package cn.ucai.fulicenter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.Response;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.data.ApiParams;
import cn.ucai.fulicenter.data.GsonRequest;
import cn.ucai.fulicenter.utils.Utils;

public class CategoryFragment extends Fragment {
    public static final String TAG = CategoryFragment.class.getName();

    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    ExpandableListView mExpandableListView;
    CategoryAdapter mAdapter;
    FuliCenterMainActivity mContext;
    int groupCount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getActivity();
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        initView(layout);
        initData();
        return layout;
    }

    private void initData() {
        getPath();
    }

    private void getPath() {
        try {
            String path = new ApiParams()
                    .getRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP);
            mContext.executeRequest(new GsonRequest<CategoryGroupBean[]>(path, CategoryGroupBean[].class,
                    responseDownloadCategoryBeanListener(), mContext.errorListener()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response.Listener<CategoryGroupBean[]> responseDownloadCategoryBeanListener() {
        return new Response.Listener<CategoryGroupBean[]>() {
            @Override
            public void onResponse(CategoryGroupBean[] categoryGroupBeen) {
                if (categoryGroupBeen != null) {
                    try {
                        mGroupList = Utils.array2List(categoryGroupBeen);
                        int i = 0;
                        for (CategoryGroupBean group : mGroupList ) {
                            mChildList.add(i,new ArrayList<CategoryChildBean>());
                            String path = new ApiParams().with(I.CategoryChild.PARENT_ID, group.getId() + "")
                                    .with(I.PAGE_ID, "0").with(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT+"")
                                    .getRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN);
                            mContext.executeRequest(new GsonRequest<CategoryChildBean[]>(path,
                                    CategoryChildBean[].class,
                                    responseDownCategoryChildListListener(i),mContext.errorListener()));
                            i++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private Response.Listener<CategoryChildBean[]> responseDownCategoryChildListListener(final int i) {
        return new Response.Listener<CategoryChildBean[]>() {
            @Override
            public void onResponse(CategoryChildBean[] categoryChildBeen) {
                groupCount++;
                if(categoryChildBeen!=null){
                    ArrayList<CategoryChildBean> childList =Utils.array2List(categoryChildBeen);
                    if(childList!=null){
                        mChildList.set(i,childList);
                    }
                }
                if(mGroupList.size()==groupCount) {
                    mAdapter.addItems(mGroupList, mChildList);
                }
            }
        };
    }


    private void initView(View layout) {
        mGroupList = new ArrayList<CategoryGroupBean>();
        mChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        mExpandableListView = (ExpandableListView) layout.findViewById(R.id.elvCategory);
        mAdapter = new CategoryAdapter(mContext, mGroupList, mChildList);
        mExpandableListView.setAdapter(mAdapter);
    }

}
