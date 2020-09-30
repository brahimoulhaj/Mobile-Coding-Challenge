package com.braoul.github30days.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.braoul.github30days.Interface.LoadMore;
import com.braoul.github30days.Model.Repository;
import com.braoul.github30days.R;
import com.bumptech.glide.Glide;

import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.loading_progress_bar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView tvRepoName;
    public TextView tvRepoDesc;
    public TextView tvRepoStars;
    public TextView tvRepoOwnerName;
    public ImageView ivRepoOwnerAvatar;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tvRepoName = itemView.findViewById(R.id.repo_name);
        tvRepoDesc = itemView.findViewById(R.id.repo_description);
        tvRepoStars = itemView.findViewById(R.id.repo_number_of_stars);
        tvRepoOwnerName = itemView.findViewById(R.id.repo_owner_name);
        ivRepoOwnerAvatar = itemView.findViewById(R.id.repo_owner_avatar);
    }
}

public class RepoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<Repository> repositories;
    int visibleThreshold = 5, lastVisibleItem, totalItemCount;

    public RepoAdapter(RecyclerView recyclerView, Activity activity, List<Repository> repositories) {
        this.activity = activity;
        this.repositories = repositories;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return repositories.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.repository_list_item, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.items_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Repository repository = repositories.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;

            viewHolder.tvRepoName.setText(repository.getName());
            viewHolder.tvRepoDesc.setText(repository.getDescription());
            if (repository.getNumStars() >= 1000) {
                double nstars = (double) repository.getNumStars() / 1000.0;
                viewHolder.tvRepoStars.setText(nstars + "k");
            } else {
                viewHolder.tvRepoStars.setText(repository.getNumStars() + "");
            }
            viewHolder.tvRepoOwnerName.setText(repository.getOwnerUsername());

            Glide.with(activity)
                    .load(repository.getOwnerAvatar())
                    .centerCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(viewHolder.ivRepoOwnerAvatar);

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);

        }
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
