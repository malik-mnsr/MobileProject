package com.hai811i.mobileproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AssistantFragment extends Fragment {


    private static final String TAG = "AssistantFragment";
    private static final String OPENAI_API_KEY = "KEY API ";   // <— remplace par la clé
    private static final MediaType JSON = MediaType.get("application/json");
    private static final MediaType AUDIO = MediaType.get("audio/m4a");


    private EditText      input;
    private ImageButton   sendBtn, micBtn;
    private ProgressBar   typing;
    private RecyclerView  list;
    private ChatAdapter   adapter;

    private final List<Message> messages = new ArrayList<>();
    private final JSONArray     history  = new JSONArray();

    private final ExecutorService ioPool      = Executors.newSingleThreadExecutor();
    private final Handler         mainHandler = new Handler(Looper.getMainLooper());

    private MediaRecorder recorder;
    private File          audioFile;
    private boolean       recording = false;


    private final OkHttpClient http = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS).build();


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assistant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {

        input    = v.findViewById(R.id.messageEditText);
        sendBtn  = v.findViewById(R.id.sendButton);
        micBtn   = v.findViewById(R.id.micButton);
        typing   = v.findViewById(R.id.typingIndicator);
        list     = v.findViewById(R.id.chatRecyclerView);

        adapter = new ChatAdapter(messages);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);


        try {
            history.put(new JSONObject()
                    .put("role", "system")
                    .put("content", "Tu es un assistant IA destiné aux professionnels de santé. Ton rôle est d’aider un médecin à :\n" +
                            "- expliquer des médicaments et traitements à ses patients,\n" +
                            "- obtenir un résumé clair d’un symptôme ou d’un cas clinique,\n" +
                            "- générer des conseils ou recommandations adaptés,\n" +
                            "- reformuler ou transcrire des notes dictées à la voix.\n" +
                            "Tu réponds de façon concise, fiable et professionnelle, sans jamais poser de diagnostic à la place du médecin.\n"));
        } catch (JSONException ignore) {}

        sendBtn.setOnClickListener(v1 -> {
            String txt = input.getText().toString().trim();
            if (!txt.isEmpty()) handleUserText(txt);
        });

        micBtn.setOnClickListener(v12 -> {
            if (recording) stopAndTranscribe();
            else requestMicAndStart();
        });
    }

    private final ActivityResultLauncher<String> micPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    granted -> { if (granted) startRecording(); else
                        Toast.makeText(getContext(),"Micro refusé",Toast.LENGTH_SHORT).show(); });

    private void requestMicAndStart() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) startRecording();
        else micPermission.launch(Manifest.permission.RECORD_AUDIO);
    }

    private void startRecording() {
        try {
            audioFile = File.createTempFile("voice", ".m4a", requireActivity().getCacheDir());
            recorder  = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(audioFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            recording = true;
            Toast.makeText(getContext(),"Enregistrement…",Toast.LENGTH_SHORT).show();
        } catch (IOException e) { Log.e(TAG,"startRecording",e);}
    }

    private void stopAndTranscribe() {
        try { recorder.stop(); recorder.release(); } catch (Exception ignore) {}
        recording = false;
        typing.setVisibility(View.VISIBLE);
        micBtn.setEnabled(false);

        ioPool.execute(() -> {
            String text = "";
            try { text = whisperTranscribe(audioFile); }
            catch (Exception e) { Log.e(TAG,"whisper",e); }

            final String finalText = text;
            mainHandler.post(() -> {
                micBtn.setEnabled(true);
                typing.setVisibility(View.GONE);
                if (!finalText.isEmpty()) {
                    input.setText(finalText);
                    input.setSelection(finalText.length());
                    handleUserText(finalText);
                } else Toast.makeText(getContext(),
                        "Transcription échouée",Toast.LENGTH_SHORT).show();
            });
        });
    }


    private String whisperTranscribe(File f) throws Exception {
        RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", f.getName(), RequestBody.create(f, AUDIO))
                .addFormDataPart("model", "whisper-1")
                .build();

        Request r = new Request.Builder()
                .url("https://api.openai.com/v1/audio/transcriptions")
                .addHeader("Authorization", OPENAI_API_KEY)
                .post(req).build();

        try (Response resp = http.newCall(r).execute()) {
            if (!resp.isSuccessful()) throw new IOException("Whisper "+resp.code());
            return new JSONObject(resp.body().string()).optString("text","");
        }
    }

    private String chatCompletion() throws Exception {
        JSONObject payload = new JSONObject()
                .put("model", "gpt-4o-mini")
                .put("messages", history);

        Request r = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", OPENAI_API_KEY)
                .post(RequestBody.create(payload.toString(), JSON))
                .build();

        try (Response resp = http.newCall(r).execute()) {
            if (!resp.isSuccessful()) throw new IOException("GPT "+resp.code());
            String content = new JSONObject(resp.body().string())
                    .getJSONArray("choices").getJSONObject(0)
                    .getJSONObject("message").getString("content");

            history.put(new JSONObject().put("role","assistant").put("content", content));
            return content.trim();
        }
    }


    private void handleUserText(String txt) {
        input.setText("");
        addMessage(txt,true);

        try { history.put(new JSONObject().put("role","user").put("content",txt)); }
        catch (JSONException ignore) {}

        typing.setVisibility(View.VISIBLE);
        ioPool.execute(() -> {
            String temp;
            try { temp = chatCompletion(); }
            catch (Exception e) { Log.e(TAG,"gpt",e); temp = "Désolé…"; }

            final String reply = temp;            // ← désormais « final »

            mainHandler.post(() -> {
                addMessage(reply, false);
                typing.setVisibility(View.GONE);
            });
        });

    }


    private void addMessage(String txt, boolean user) {
        messages.add(new Message(txt,user));
        adapter.notifyItemInserted(messages.size()-1);
        list.scrollToPosition(messages.size()-1);
    }

    static class Message {
        final String txt; final boolean user;
        Message(String t, boolean u){ txt=t; user=u; }
    }

    static class ChatVH extends RecyclerView.ViewHolder {
        final TextView tv;
        ChatVH(@NonNull View v){ super(v); tv = v.findViewById(R.id.textMessage); }
    }

    static class ChatAdapter extends RecyclerView.Adapter<ChatVH> {
        private final List<Message> data;
        ChatAdapter(List<Message>d){ data=d; }

        @NonNull @Override public ChatVH onCreateViewHolder(@NonNull ViewGroup p,int v){
            View item = LayoutInflater.from(p.getContext())
                    .inflate(R.layout.item_chat_message,p,false);
            return new ChatVH(item);
        }
        @Override public void onBindViewHolder(@NonNull ChatVH h,int pos){
            Message m = data.get(pos);
            h.tv.setText(m.txt);
            h.tv.setBackgroundResource(m.user? R.drawable.bubble_user : R.drawable.bubble_bot);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)h.tv.getLayoutParams();
            lp.gravity = m.user? Gravity.END : Gravity.START;
            h.tv.setLayoutParams(lp);
        }
        @Override public int getItemCount(){ return data.size(); }
    }
}