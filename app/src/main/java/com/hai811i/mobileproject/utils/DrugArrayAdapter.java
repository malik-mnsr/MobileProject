package com.hai811i.mobileproject.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.hai811i.mobileproject.entity.DrugReference;

import java.util.Collections;
import java.util.List;

public class DrugArrayAdapter extends ArrayAdapter<DrugReference> {
    private final List<DrugReference> drugs;

    public DrugArrayAdapter(Context context, List<DrugReference> drugs) {
        super(context, android.R.layout.simple_dropdown_item_1line, drugs);
        this.drugs = drugs;
    }

    @Override
    public Filter getFilter() {
        return new DrugFilter(this, drugs);
    }

    private static class DrugFilter extends Filter {
        private final DrugArrayAdapter adapter;
        private final List<DrugReference> originalList;

        public DrugFilter(DrugArrayAdapter adapter, List<DrugReference> originalList) {
            this.adapter = adapter;
            this.originalList = originalList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = Collections.emptyList();
                results.count = 0;
            } else {
                // Return the full list - actual filtering is done server-side
                results.values = originalList;
                results.count = originalList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.clear();
            if (results.count > 0) {
                adapter.addAll((List<DrugReference>) results.values);
            }
            adapter.notifyDataSetChanged();
        }
    }
}