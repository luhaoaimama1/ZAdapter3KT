package zone.com.zadapter3kt.core

/**
 *[2018/11/13] by Zone
 */
enum class Page(val pageIndex: Int, val traceString: String) {
    HOME(1, ""),
    STICKY(2, ""),
    BLUE(3, "");


    fun isHome(page: Page): Boolean = (page == HOME)
}