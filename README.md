# ZAdapter3

#### [中文版文档](./README-cn.md)

# advantages

-[x] RecyclerView chain calls

-[x] Support break absorb effect

-[x] Reusable, resource id write inside the reusable class

-[x] Helper class chain calls and extensible

-[x] Empty data support the view

-[x] Support the Diff fast

-[x] Add quick way to support

-[x] Support and ZRefresh linkage

> Why use a helper rather than bindview?

> Reason: can be customized packaging logic , not strong, can even write;

# Usage

### JicPack
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
Step 2. Add the dependency
> compile 'com.github.luhaoaimama1:ZAdapter3:[Latest release](https://github.com/luhaoaimama1/ZAdapter3/releases)'


# Easy use:

1.Easy use

```
    IAdapter<String> muliAdapter = new QuickRcvAdapter(this, mDatas){
                @Override
                protected int getItemViewType2(int dataPosition) {
                //This method can not write for the default Settings and the default value to return Wrapper.DEFAULT_VALUE(-1)
                    return dataPosition % 2;
                }
            };
    muliAdapter
                .addViewHolder(new LeftDelegates())//default
                .addViewHolder(0, new LeftDelegates()) //Many layout
                .addViewHolder(1, new RightDelegates())//Many layout
                .addHeaderHolder(R.layout.header_simple)//resources
                .addHeaderHolder(ViewDelegates footer)//Can also be the
                .addFooterHolder(R.layout.footer_simple)//resources
                .addFooterHolder(ViewDelegates footer)//Can also be the
                .addEmptyHold(R.layout.empty)//Also support empty resources
                .addEmptyHold(ViewDelegates emtpy)//Also support empty
                .setOnItemClickListener(new IAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ViewGroup parent, View view, int position) {
                        System.out.println("By clicking->onItemClick" + position);
                    }
                })
                .setOnItemLongClickListener(new IAdapter.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(ViewGroup parent, View view, int position) {
                        System.out.println("By clicking->onItemLongClick:" + position);
                        return true;
                    }
                })
                .addOnScrollListener(scroller = new OnScrollRcvListenerEx(new OnScrollRcvListenerEx.LoadMoreCallback() {
                    @Override
                    public void loadMore() {
                    //And I was the main reason for this class ZRefresh library compatible.I will entrust the loading processing here!
                    }
                }))
                .relatedList(rv)
                /**
                * Must first set the layoutManager to set up this way.
                * addItemDecoration(MarginItemDecoration itemDecoration)
                * You can also use this method to get itemDecoration,contains hasRight(boolean hasRight),hasBottom(boolean hasBottom)...
                * More advanced by setOnTransformListener custom intervals
                */
                .addItemDecoration(10);
```

2.ViewDelegates's use

```
public class LeftDelegates extends ViewDelegates<String> {

    @Override
    public int getLayoutId() {
        return R.layout.item_left;
    }

    @Override
    public void fillData(int postion, String data, Helper helper) {
//        helper.setText(R.id.tv, data);

        ExtraHelper.wrapper(helper).setText(R.id.tv, data).heihei().heihei2()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("helper click test ");
                    }
                }, new int[]{R.id.tv, R.id.ll_main});
        ;
    }
}
```

3.The expansion of the Helper skills: decorative pattern is extended technique + chain calls

> ExtraHelper.wrapper(helper).setText(R.id.tv, data).heihei().heihei2();

```
public class ExtraHelper<T extends ExtraHelper> extends Helper<T> {


    protected ExtraHelper(Helper helper) {
        super(helper.getContext(), helper.getHolder(), helper.getAdapter());
        child = (T) this;
    }

    public static ExtraHelper<ExtraHelper> wrapper(Helper helper) {
        return new ExtraHelper(helper);
    }

    public T heihei() {
        System.out.println("heihei!");
        checkChild();
        return child;
    }

    public T heihei2() {
        System.out.println("heihei2!");
        checkChild();
        return child;
    }
}

```

4.Global configuration

    //Global replacement load more
    QuickConfig.build().setLoadMoreDelegates(ILoadMoreDelegates iLoadMoreDelegates).perform();

5.Support and ZRefresh linkage

> please look demo

6.Add quick way to support

```
    mAdapter.notifyItemInsertedEx(mDatas.size() - 1);

    void scrollToData(T o);

    void scrollToPosition(int position);

    void scrollToLast();

    void scrollToHF(ViewDelegates hf);

    IAdapter removeFooterHolder(ViewDelegates footer);

    IAdapter removeFooterHolder(ViewDelegates footer,boolean notify);

    mAdapter.addItemDecoration(10)
    ...
```

7.Support the Diff fast

```
        muliAdapter.diffSetKeyframe();//A key frame
        mDatas.add(7, "insert diff");
        for (int i = 0; i < 3; i++) {
            mDatas.remove(1 + i);
        }
        muliAdapter.diffCalculate(new DiffCallBack<String>() {
            @Override
            public boolean areContentsTheSame(String oldItem, String newItem) {
                return oldItem.equals(newItem);
            }
        });//Calculate the best written on the thread
        muliAdapter.diffNotifyDataSetChanged();//notify
```

8.Support break absorb effect

![](https://ww1.sinaimg.cn/large/006tNc79ly1fifdm9jzucg307i0dcal7.gif)

```
rv.addOnScrollListener(new AbsorbOnScrollListener(vp, 3, 6, 9));

        muliAdapter = new QuickRcvAdapter(this, mDatas) {
            @Override
            protected int getItemViewType2(int dataPosition) {
                if (dataPosition == 3)
                    return 1;
                else if (dataPosition == 6)
                    return 2;
                else if (dataPosition == 9)
                    return 3;
                else
                    return 0;
            }
        };
        muliAdapter
                .addViewHolder(new LeftDelegates())//default
                .addViewHolder(0, new LeftDelegates()) //多部剧 注释开启即可
                .addViewHolder(1, new AbsorbDelegates())//多部剧 注释开启即可
                .addViewHolder(2, new AbsorbDelegates2())//多部剧 注释开启即可
                .addViewHolder(3, new AbsorbDelegates())//多部剧 注释开启即可
```



# Update log

## 1.0.7

  * Repair not filling all kinds of generic and cause the getView method of generic error


## 1.0.5

  * Support break absorb effect
  * fix MarginItemDecoration bug,and support ViewDelegates's reduceDectorRect and dectorRect change dector ;


## 1.0.2

  * 1. RecyclerView chain calls
  * 2. Reusable, resource id write inside the reusable class
  * 3. Helper class chain calls and extensible
  * 4. Empty data support the view
  * 5. Support the Diff fast
  * 6. Add quick way to support
  * 7. Rapid support four state loading,complete,end,fail Support and ZRefresh linkage
  * 8. With the support of the layout, and the special layout of the demo, the layout of the reference and rolling speed
  * 9. Decoration fast support, support does it include the border, up and down or so alone and so on, the more advanced contains custom monitoring

# Reference&Thanks：
https://github.com/JoanZapata/base-adapter-helper

https://github.com/cundong/HeaderAndFooterRecyclerView

https://github.com/GcsSloop/diycode