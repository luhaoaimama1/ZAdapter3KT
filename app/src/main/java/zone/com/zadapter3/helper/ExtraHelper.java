package zone.com.zadapter3.helper;

import com.zone.adapter3.bean.Holder;

/**
 * [2017] by Zone
 * 包装类  ,并且 returen this 泛型属性
 *
 * 代理类也可以
 */
public class ExtraHelper<T extends ExtraHelper> extends Holder<T> {


    protected ExtraHelper(Holder holder) {
        super( holder.itemView);
        child = (T) this;
    }

    public static ExtraHelper<ExtraHelper> wrapper(Holder holder) {
        return new ExtraHelper(holder);
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
