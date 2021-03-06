package cn.huangdayu.almanac.utils;

import cn.huangdayu.almanac.dto.AlmanacDTO;
import cn.huangdayu.almanac.dto.IslamicDTO;


/**
 * 回历工具类
 * 伊斯兰教的历法，又称希吉来历，在我国也叫回回历或回历
 *
 * @author huangdayu
 * @update 2020-03-15
 */
public class IslamicCalendarUtils {
    /****
     * 回历计算方法
     * @param julianDays
     */
    public static IslamicDTO setIslamicCalendar(int julianDays) {
        // 以下算法使用Excel测试得到,测试时主要关心年临界与月临界
        int z, y, m, d;
        d = julianDays + 503105;
        // 10631为一周期(30年)
        z = (int) Math.floor(d / 10631);
        d -= z * 10631;
        // 加0.5的作用是保证闰年正确(一周中的闰年是第2,5,7,10,13,16,18,21,24,26,29年)
        y = (int) Math.floor((d + 0.5) / 354.366);
        d -= (int) Math.floor(y * 354.366 + 0.5);
        // 分子加0.11,分母加0.01的作用是第354或355天的的月分保持为12月(m=11)
        m = (int) Math.floor((d + 0.11) / 29.51);
        d -= (int) Math.floor(m * 29.5 + 0.5);
        IslamicDTO islamicDTO = new IslamicDTO();
        islamicDTO.setYear(z * 30 + y + 1);
        islamicDTO.setMonth(m + 1);
        islamicDTO.setDay(d + 1);
        return islamicDTO;
    }

}
