package zone.com.zadapter3.helper;

import com.zone.adapter3.helper.Helper;

/**
 * [2017] by Zone
 * 包装类  ,并且 returen this 泛型属性
 */
public class ExtraHelper extends Helper<ExtraHelper> {


    private ExtraHelper(Helper helper) {
        super(helper.getContext(), helper.getHolder(),helper.getAdapter());
        child = this;
    }

    public static ExtraHelper wrapper(Helper helper){
        return new ExtraHelper(helper);
    }

    public ExtraHelper heihei() {
        System.out.println("heihei!");
        checkChild();
        return child;
    }

    public ExtraHelper heihei2() {
        System.out.println("heihei2!");
        checkChild();
        return child;
    }
}
