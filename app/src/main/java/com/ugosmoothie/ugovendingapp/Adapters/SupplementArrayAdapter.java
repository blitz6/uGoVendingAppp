package com.ugosmoothie.ugovendingapp.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ugosmoothie.ugovendingapp.Data.Liquid;
import com.ugosmoothie.ugovendingapp.Data.Supplement;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Michelle on 3/16/2016.
 */
public class SupplementArrayAdapter extends ArrayAdapter<Supplement> {

    HashMap<Supplement, Long> mIdMap = new HashMap<Supplement, Long>();

    public SupplementArrayAdapter(Context context, int textViewResourceId,
                              List<Supplement> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), objects.get(i).getId());
        }
    }

    @Override
    public long getItemId(int position) {
        Supplement item = getItem(position);
        return item.getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
