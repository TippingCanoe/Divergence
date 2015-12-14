package com.tippingcanoe.divergence;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class DivergenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    RecyclerView recyclerView;
    List<IdentifiedSection> sections;

    public DivergenceAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        sections = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        SectionAndPosition sectionAndPosition = convertGlobalPositionToSectionPosition(position);

        if (sectionAndPosition != null) {
            sectionAndPosition.section.section.getItemViewType(sectionAndPosition.positionInSection);
        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (IdentifiedSection identifiedSection : sections) {
            RecyclerView.ViewHolder viewHolder = identifiedSection.section.onCreateViewHolder(parent, viewType);

            if (viewHolder != null) {
                return viewHolder;
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SectionAndPosition sectionAndPosition = convertGlobalPositionToSectionPosition(position);

        if (sectionAndPosition != null) {
            sectionAndPosition.section.section.onBindViewHolder(holder, sectionAndPosition.positionInSection);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        SectionAndPosition sectionAndPosition = convertGlobalPositionToSectionPosition(position);

        if (sectionAndPosition != null) {
            sectionAndPosition.section.section.onBindViewHolder(holder, sectionAndPosition.positionInSection, payloads);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;

        for (IdentifiedSection identifiedSection : sections) {
            count += identifiedSection.section.getItemCount();
        }

        return count;
    }

    @Override
    public long getItemId(int position) {
        SectionAndPosition sectionAndPosition = convertGlobalPositionToSectionPosition(position);

        if (sectionAndPosition != null) {
            return sectionAndPosition.section.section.getItemId(sectionAndPosition.positionInSection);
        }

        return super.getItemId(position);
    }

    public void addSection(SectionAdapter section, String identifier, int position, Importance importance) {
        IdentifiedSection identifiedSection = new IdentifiedSection(section, identifier, importance, position);

        int insertionListPosition = 0;
        int insertionSectionPosition = position;

        if (sections.size() > position) {
            IdentifiedSection currentSectionInPosition = sections.get(position);

            if (currentSectionInPosition.importance == Importance.CRITICAL) {
                throw new InvalidParameterException("Cannot add section at " + position + " as `" + currentSectionInPosition.identifier + "` is already in that position with critical importance.");
            }

            for (int i = 0; i < sections.size(); i++) {
                if (sections.get(i).requestedPosition <= position || sections.get(i).importance.getRank() > importance.getRank()) {
                    insertionSectionPosition = i + 1;
                    insertionListPosition += sections.get(i).section.getItemCount();
                } else {
                    break;
                }
            }
        } else {
            insertionSectionPosition = sections.size();
            insertionListPosition = getItemCount();
        }

        sections.add(insertionSectionPosition, identifiedSection);
        notifyItemRangeInserted(insertionListPosition, section.getItemCount());
    }


    public void removeSection(String identifier) {
        int removalListPosition = 0;

        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).identifier.equals(identifier)) {
                IdentifiedSection removedSection = sections.get(i);
                sections.remove(i);
                notifyItemRangeRemoved(removalListPosition, removedSection.section.getItemCount());

                return;
            }

            removalListPosition += sections.get(i).section.getItemCount();
        }
    }

    @Nullable
    protected SectionAndPosition convertGlobalPositionToSectionPosition(int globalPosition) {
        for (int i = 0; i < sections.size(); i++) {
            if (globalPosition - sections.get(i).section.getItemCount() < 0) {
                return new SectionAndPosition(sections.get(i), globalPosition);
            }

            globalPosition -= sections.get(i).section.getItemCount();
        }

        return null;
    }

    protected class SectionAndPosition {
        IdentifiedSection section;
        int positionInSection;

        public SectionAndPosition(IdentifiedSection section, int positionInSection) {
            this.section = section;
            this.positionInSection = positionInSection;
        }
    }

    protected class IdentifiedSection {
        SectionAdapter section;
        String identifier;
        Importance importance;
        int requestedPosition;

        public IdentifiedSection(SectionAdapter section, String identifier, Importance importance, int requestedPosition) {
            this.section = section;
            this.identifier = identifier;
            this.importance = importance;
            this.requestedPosition = requestedPosition;
        }
    }
}
