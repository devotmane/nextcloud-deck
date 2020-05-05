package it.niedermann.nextcloud.deck.ui.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import it.niedermann.nextcloud.deck.R;
import it.niedermann.nextcloud.deck.databinding.DialogFilterAssigneesBinding;
import it.niedermann.nextcloud.deck.model.User;
import it.niedermann.nextcloud.deck.model.internal.FilterInformation;
import it.niedermann.nextcloud.deck.persistence.sync.SyncManager;
import it.niedermann.nextcloud.deck.ui.MainViewModel;

import static it.niedermann.nextcloud.deck.persistence.sync.adapters.db.util.LiveDataHelper.observeOnce;
import static it.niedermann.nextcloud.deck.util.DimensionUtil.dpToPx;
import static java.util.Objects.requireNonNull;

public class FilterAssigneesFragment extends Fragment implements SelectionListener<User> {

    private LiveData<FilterInformation> filterInformationDraft;
    private DialogFilterAssigneesBinding binding;
    private MainViewModel mainViewModel;
    private FilterViewModel filterViewModel;
    private UserFilterAdapter userAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogFilterAssigneesBinding.inflate(requireActivity().getLayoutInflater());
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        final SyncManager syncManager = new SyncManager(requireActivity());

        this.filterInformationDraft = filterViewModel.getFilterInformationDraft();
        observeOnce(syncManager.findProposalsForUsersToAssign(mainViewModel.getCurrentAccount().getId(), mainViewModel.getCurrentBoardLocalId()), requireActivity(), (users) -> {
            userAdapter = new UserFilterAdapter(dpToPx(requireContext(), R.dimen.avatar_size), mainViewModel.getCurrentAccount(), users, requireNonNull(this.filterInformationDraft.getValue()).getUsers(), this);
            binding.users.setNestedScrollingEnabled(false);
            binding.users.setAdapter(userAdapter);
        });

        return binding.getRoot();
    }

    @Override
    public void onItemSelected(User item) {
        filterViewModel.addFilterInformationUser(item);
    }

    @Override
    public void onItemDeselected(User item) {
        filterViewModel.removeFilterInformationUser(item);
    }
}