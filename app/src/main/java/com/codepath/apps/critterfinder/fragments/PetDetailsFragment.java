package com.codepath.apps.critterfinder.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.critterfinder.R;
import com.codepath.apps.critterfinder.models.PetModel;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by smacgregor on 2/29/16.
 */
public class PetDetailsFragment extends Fragment {

    private static final String ARGUMENT_PET = "ARGUMENT_PET";
    private PetDetailsFragment.OnDetailsFragmentListener mDetailsFragmentListener;
    private View mFragmentView; // View for the inflated Fragment

    @Bind(R.id.text_pet_info) TextView mPetInfo;
    @Bind(R.id.text_pet_description) TextView mPetDescription;
    @Bind(R.id.text_pet_breed) TextView mPetBreed;
    @Bind(R.id.text_shelter_contact_name) TextView mShelterContactName;
    @Bind(R.id.text_shelter_contact_phone) TextView mShelterContactPhone;
    @Bind(R.id.text_shelter_location) TextView mShelterLocation;
    @Bind(R.id.text_shelter_email) TextView mShelterEmail;
    @Bind(R.id.email_icon) ImageView mEmailIcon;
    @Bind(R.id.phone_icon) ImageView mPhoneIcon;

    private PetModel mPet;

    public static PetDetailsFragment newInstance(PetModel pet) {
        PetDetailsFragment petDetailsFragment = new PetDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGUMENT_PET, Parcels.wrap(pet));
        petDetailsFragment.setArguments(args);
        return petDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPet = Parcels.unwrap(getArguments().getParcelable(ARGUMENT_PET));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_pet_details, container, false);
        ButterKnife.bind(this, mFragmentView);
        setupPetDetailsView();
        mEmailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmail();
            }
        });
        mPhoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCall();
            }
        });
        mShelterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmail();
            }
        });
        return mFragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsFragmentListener) {
            mDetailsFragmentListener = (OnDetailsFragmentListener)context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    private void setupPetDetailsView() {
        mPetInfo.setText(mPet.getSizeSexAge());
        mPetBreed.setText(mPet.getBreedFullName());
        mPetDescription.setText(mPet.getDescription());
        mShelterContactName.setText(mPet.getContactName());
        mShelterContactPhone.setText(mPet.getContactPhone());
        mShelterLocation.setText(mPet.getLocation());
        mShelterEmail.setText(mPet.getContactEmail());
    }

    // Call the shelter
    public void onCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mPet.getContactPhone()));
        startActivity(intent);
    }

    // Create an Email to the shelter
    public void onEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");

        final String subject = getString(R.string.contact_pet_subject, mPet.getName());
        final String body = getString(R.string.contact_pet_body, mPet.getName());

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mPet.getContactEmail()});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            Snackbar.make(mFragmentView, getString(R.string.contact_no_email_client), Snackbar.LENGTH_LONG).show();
        }
    }

    public interface OnDetailsFragmentListener {
        void onContactShelter(PetModel pet, View transitionView);
    }

}
