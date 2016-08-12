package com.pipit.agc.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pipit.agc.R;
import com.pipit.agc.controller.GeofenceController;
import com.pipit.agc.fragment.LocationListFragment;
import com.pipit.agc.model.Gym;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    private static final String TAG = "LocationListAdapter";
    private final List<Gym> _gymList;
    private final LocationListFragment mFrag;
    private GeofenceController.GeofenceControllerListener mListener;

    public LocationListAdapter(List<Gym> gyms, GeofenceController.GeofenceControllerListener listener, LocationListFragment frag) {
        mFrag = frag;
        _gymList = gyms;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Show layout for empty gym
        if (_gymList.get(position).isEmpty){
            holder.mRemoveButton.setVisibility(View.GONE);
            holder.mIdView.setVisibility(View.GONE);
            holder.mContentView.setVisibility(View.GONE);
            holder.mNameTextView.setText("Touch to add gym");
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams)holder.mNameTextView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            holder.mNameTextView.setLayoutParams(layoutParams);
        }else {
            holder.mItem = _gymList.get(position);
            //holder.mIdView.setText("Gym " + Integer.toString(_gymList.get(position).proxid));
            holder.mIdView.setText(mFrag.getContext().getString(R.string.gym));
            holder.mNameTextView.setText(_gymList.get(position).name);
            if (_gymList.get(position).address.equals(mFrag.getResources().getString(R.string.no_address_default))) {
                //If there is no address, use the coordinates
                holder.mContentView.setText(_gymList.get(position).location.getLongitude()
                        + " " + _gymList.get(position).location.getLatitude());
            } else {
                holder.mContentView.setText(_gymList.get(position).address + " ");
            }
        }
        holder.mClickableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked add gym");
                Toast.makeText(mFrag.getContext(), mFrag.getString(R.string.launchmap), Toast.LENGTH_SHORT);
                mFrag.startPlacePicker(_gymList.get(position).proxid);
            }
        });

        holder.mRemoveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "Removing a gym");
                GeofenceController.getInstance().removeGeofencesById(_gymList.get(position).proxid, mListener);
                //((AllinOneActivity) mFrag.getActivity()).removeGeofencesById(position + 1);
                //_gymList.set(position, new Gym());
            }

        });

    }

    @Override
    public int getItemCount() {
        return _gymList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final RelativeLayout mClickableLayout;
        public final TextView mNameTextView;
        public final Button mRemoveButton;

        public Gym mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.gym_id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mClickableLayout = (RelativeLayout) view.findViewById(R.id.location_description);
            mRemoveButton = (Button) view.findViewById(R.id.removeButton);
            mNameTextView = (TextView) view.findViewById(R.id.gym_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


}
