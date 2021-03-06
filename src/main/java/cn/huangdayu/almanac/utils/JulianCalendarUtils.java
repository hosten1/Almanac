package cn.huangdayu.almanac.utils;

import cn.huangdayu.almanac.dto.TimeZoneDTO;

import java.util.Calendar;


/**
 * 儒略历工具类
 * 儒略历（Julian calendar）是由罗马共和国独裁官儒略·恺撒（即盖乌斯·尤里乌斯·凯撒）采纳数学家兼天文学家索西琴尼的计算后，
 * 于公元前45年1月1日起执行的取代旧罗马历法的一种历法。儒略历中，一年被划分为12个月，大小月交替；四年一闰，平年365日，
 * 闰年366日为在当年二月底增加一闰日，年平均长度为365.25日。由于实际使用过程中累积的误差随着时间越来越大，
 * 1582年教皇格里高利十三世颁布、推行了以儒略历为基础改善而来的格里历，即沿用至今的公历。
 *
 * @author huangdayu
 * @update 2020-03-15
 */
public class JulianCalendarUtils {
    // private final String[] weeks = { "日", "一", "二", "三", "四", "五", "六", "七" };

    /***
     * 输入年，月，日算出儒略日 http://www.ilovematlab.cn/thread-19002-1-1.html
     *
     * @param year
     *            年
     * @param month
     *            月
     * @param day
     *            日
     * @return 儒略日
     */
    public static int getJuLian(int year, int month, int day) {
        return day - 32075 + 1461 * (year + 4800 + (month - 14) / 12) / 4 + 367 * (month - 2 - (month - 14) / 12 * 12) / 12
                - 3 * ((year + 4900 + (month - 14) / 12) / 100) / 4;
    }

    public static int getJuLian(Calendar calendar) {
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        return getJuLian(y, m, d);
    }

    /***
     * 取某世纪年的1月1日的儒略日数，如1900,1800,2000,2100
     *
     * @param year
     *            公元1百年
     * @return 1757583
     */
    public static int getJuLianByYear(int year) {
        int quZhen = year / 100;
        int quYu = year % 100;
        String Str;

        if (0 == quYu && quZhen > 0 && year != 0) {// 整除100时取整减1 ：如果是1000年，则算出900年的儒略日
            Str = String.valueOf(quZhen - 1) + "00";
        } else {
            Str = String.valueOf(quZhen) + "00";
        }

        if (year <= 0) {
            Str = String.valueOf((-year - 1) / 100 - 1) + "00";
        }

        // System.out.println(Str);
        return getJuLian(Integer.valueOf(Str), 1, 1);
    }

    /***
     * 输入2017 返回1900
     *
     * @param year
     * @return
     */
    public static int getYear_INT(int year) {

        int quZhen = year / 100;
        int quYu = year % 100;
        String Str;

        if (quZhen > 0 && year != 0) {// 整除100时取整减1 ：如果是1000年，则算出900年的儒略日
            Str = String.valueOf(quZhen - 1) + "00";
        } else {
            Str = String.valueOf(quZhen) + "00";
        }

        if (year <= 0) {
            Str = String.valueOf((-year - 1) / 100 - 1) + "00";
        }

        return Integer.valueOf(Str);
    }

    /****
     * 公历转儒略日
     *
     * @return
     */
    public static double getJulianDayNumber(int year, int month) {
        return getJulianDayNumber(year, month, 1, 12, 0, 0.1);
    }

    /****
     * 公历转儒略日
     *
     * @return
     */
    public static double getJulianDayNumber(int year, int month, int day, int hour, int minute, double second) {
        return getJulianDayNumber(year, month, day + ((second / 60 + minute) / 60 + hour) / 24);
    }

    /***
     * 公历转儒略日
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static double getJulianDayNumber(int year, int month, double day) {
        if (month > 12) {
            month = month % 12;
            year = year + month / 12;
        }
        int n = 0, G = 0;
        if (year * 372 + month * 31 + Math.floor(day) >= 588829) {
            G = 1; // 判断是否为格里高利历日1582*372+10*31+15
        }
        if (month <= 2) {
            month += 12;
            year--;
        }
        if (G != 0) {
            n = (int) Math.floor(year / 100);
            n = 2 - n + (int) Math.floor(n / 4); // 加百年闰
        }
        return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + n - 1524.5;
    }

    /****
     * 儒略日数转公历
     *
     * @param julianDays
     * @return
     */
    public static TimeZoneDTO julianDaysToTimeZone(double julianDays) {
        int year, month, day, hour, minute;
        double second;
        int D = (int) Math.floor(julianDays + 0.5), c;
        double F = julianDays + 0.5 - D; // 取得日数的整数部份A及小数部分F

        if (D >= 2299161) {
            c = (int) Math.floor((D - 1867216.25) / 36524.25);
            D += 1 + c - Math.floor(c / 4);
        }
        D += 1524;
        year = (int) Math.floor((D - 122.1) / 365.25);// 年数
        D -= Math.floor(365.25 * year);
        month = (int) Math.floor(D / 30.601); // 月数
        D -= Math.floor(30.601 * month);
        day = D; // 日数
        if (month > 13) {
            month -= 13;
            year -= 4715;
        } else {
            month -= 1;
            year -= 4716;
        }
        // 日的小数转为时分秒
        F *= 24;
        hour = (int) Math.floor(F);
        F -= hour;
        F *= 60;
        minute = (int) Math.floor(F);
        F -= minute;
        F *= 60;
        second = F;

        return new TimeZoneDTO(year, month, day, hour, minute, (int) second, 0);
    }

    /***
     * 日期转为串
     *
     * @param timeZoneDTO
     * @return
     */
    @Deprecated
    public static String julianDateByTimeZone(TimeZoneDTO timeZoneDTO) {
        String y = "     " + timeZoneDTO.getYear(), m = "0" + timeZoneDTO.getMonth(), d = "0" + timeZoneDTO.getDay();
        int h = timeZoneDTO.getHour(), mm = timeZoneDTO.getMinute(), s = (int) Math.floor(timeZoneDTO.getSecond() + .5);
        if (s >= 60) {
            s -= 60;
            mm++;
        }
        if (mm >= 60) {
            mm -= 60;
            h++;
        }
        String hStr, mStr, sStr;
        hStr = "0" + h;
        mStr = "0" + mm;
        sStr = "0" + s;
        y = CommonUtils.subString(y, y.length() - 5);
        m = CommonUtils.subString(m, m.length() - 2);
        d = CommonUtils.subString(d, d.length() - 2);
        hStr = CommonUtils.subString(hStr, hStr.length() - 2);
        mStr = CommonUtils.subString(mStr, mStr.length() - 2);
        sStr = CommonUtils.subString(sStr, sStr.length() - 2);
        return y + "-" + m + "-" + d + " " + hStr + ":" + mStr + ":" + sStr;
    }

    /****
     * JD转为串
     *
     * @param jd
     * @return
     */
    public static String julianDays2str(double jd) {
        return julianDaysToTimeZone(jd).getDateTime();
    }

    /***
     * 提取jd中的时间(去除日期)
     *
     * @param jd
     * @return
     */
    public static String getJulianTime(double jd) {

        int h, m, s;
        jd += 0.5;
        jd = (jd - Math.floor(jd));
        s = (int) Math.floor(jd * 86400 + 0.5);
        h = (int) Math.floor(s / 3600);
        s -= h * 3600;
        m = (int) Math.floor(s / 60);
        s -= m * 60;
        String hStr, mStr, sStr;
        hStr = "0" + h;
        mStr = "0" + m;
        sStr = "0" + s;
        return CommonUtils.subString(hStr, hStr.length() - 2, hStr.length()) + ":"
                + CommonUtils.subString(mStr, mStr.length() - 2, mStr.length()) + ":"
                + CommonUtils.subString(sStr, sStr.length() - 2, sStr.length());
    }

    public static String getJulianToDateTime(double julianDay) {
        return DateTimeUtils.dateFormat(julianDaysToTimeZone(julianDay).getCalendar(), "yyyy-MM-dd HH:mm:ss.SS");
    }

    /****
     * 星期计算
     *
     * @param jd
     * @return
     */
    protected int getWeek(double jd) {
        return (int) Math.floor(jd + 1.5 + 7000000) % 7;
    }

    /****
     * 求y年m月的第n个星期w的儒略日数
     *
     * @param year
     * @param month
     * @param n
     * @param week
     * @return
     */
    public static int julianDaysByWeek(int year, int month, int n, int week) {
        // =================可能有错

        // 求y年m月的第n个星期w的儒略日数
        double jd = getJulianDayNumber(year, month, 1.5); // 月首儒略日
        double w0 = (jd + 1 + 7000000) % 7; // 月首的星期
        int r = (int) (jd - w0 + 7 * n + week);
        // jd-w0+7*n是和n个星期0,起算下本月第一行的星期日(可能落在上一月)。加w后为第n个星期w
        if (week >= w0) {
            r -= 7; // 第1个星期w可能落在上个月,造成多算1周,所以考虑减1周
        }
        if (n == 5) {
            month++;
            if (month > 12) {
                month = 1;
                year++;
            } // 下个月
            if (r >= getJulianDayNumber(year, month, 1.5)) {
                r -= 7; // r跑到下个月则减1周
            }
        }
        return r;
    }
}
