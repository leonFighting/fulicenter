package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.CategoryDetailActivity;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by leon on 2016/6/17.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {

    Context mContext;
    ArrayList<CategoryGroupBean> mCategoryGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mCategoryChildList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> mCategoryGroupList, ArrayList<ArrayList<CategoryChildBean>> mCategoryChildList) {
        this.mContext = mContext;
        this.mCategoryGroupList = mCategoryGroupList;
        this.mCategoryChildList = mCategoryChildList;
    }

    @Override
    public int getGroupCount() {
        return mCategoryGroupList==null?0:mCategoryGroupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mCategoryChildList==null||mCategoryChildList.get(i)==null?0:mCategoryChildList.get(i).size();
    }

    @Override
    public CategoryGroupBean getGroup(int i) {
        return mCategoryGroupList.get(i);
    }

    @Override
    public CategoryChildBean getChild(int i, int i1) {
        return mCategoryChildList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View layout, ViewGroup viewGroup) {
        CategoryItemViewHolder holder = null;
        if (layout == null) {
            layout = View.inflate(mContext,R.layout.item_category,null);
            holder = new CategoryItemViewHolder();
            holder.mnivCategoryImg = (NetworkImageView) layout.findViewById(R.id.niv_item_categoryImg);
            holder.mtvCategoryName = (TextView) layout.findViewById(R.id.tv_item_categoryName);
            holder.mivCategoryExpandOn = (ImageView) layout.findViewById(R.id.iv_categoryExpandOn);
            layout.setTag(holder);
        } else {
            holder = (CategoryItemViewHolder) layout.getTag();
        }
        CategoryGroupBean categoryGroupBean = getGroup(i);
        holder.mtvCategoryName.setText(categoryGroupBean.getName());
        if(b){
            holder.mivCategoryExpandOn.setImageResource(R.drawable.expand_off);
        }else{
            holder.mivCategoryExpandOn.setImageResource(R.drawable.expand_on);
        }
        ImageUtils.setCategoryGroupImg(categoryGroupBean.getImageUrl(),holder.mnivCategoryImg);
        return layout;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View layout, ViewGroup viewGroup) {
        CategoryChildItemViewHolder holder = null;
        if (layout == null) {
            layout = View.inflate(mContext, R.layout.item_category_child, null);
            holder = new CategoryChildItemViewHolder();
            holder.mLayoutCategoryChild = (RelativeLayout) layout.findViewById(R.id.layout_category_child);
            holder.mnivCategoryChildImg = (NetworkImageView) layout.findViewById(R.id.nivCategoryChildImg);
            holder.mtvCategoryChildName = (TextView) layout.findViewById(R.id.tvCategoryChildName);
            layout.setTag(holder);
        } else {
            holder = (CategoryChildItemViewHolder) layout.getTag();
        }
        final CategoryChildBean child = getChild(i, i1);
        holder.mtvCategoryChildName.setText(child.getName());
        ImageUtils.setCategoryChildImg(child.getImageUrl(), holder.mnivCategoryChildImg);

        holder.mLayoutCategoryChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, CategoryDetailActivity.class)
                        .putExtra(I.CategoryChild.CAT_ID, child.getId()));
            }
        });
        return layout;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void addItems(ArrayList<CategoryGroupBean> mGroupList,ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        this.mCategoryGroupList.addAll(mGroupList);
        this.mCategoryChildList.addAll(mChildList);
        notifyDataSetChanged();
    }

    class CategoryItemViewHolder {
        NetworkImageView mnivCategoryImg;
        TextView mtvCategoryName;
        ImageView mivCategoryExpandOn;
    }

    class CategoryChildItemViewHolder {
        RelativeLayout mLayoutCategoryChild;
        NetworkImageView mnivCategoryChildImg;
        TextView mtvCategoryChildName;
    }

}
