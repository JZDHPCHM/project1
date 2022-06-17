package gbicc.irs.main.rating.support;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wsp.framework.security.support.SecurityRole;
import org.wsp.framework.security.util.SecurityUtil;

public class CommonUtils{

	/**
	 * 字符串转换为BigDecimal
	 * @param num 字符串数值
	 * @return
	 */
	public static BigDecimal getBigDecimal(String num) {
		if(StringUtils.isEmpty(num)) return BigDecimal.ZERO;
		BigDecimal value = new BigDecimal(num);
		value = value.setScale(6,BigDecimal.ROUND_HALF_UP); 
		return value;
	}
	
	public static String getString(Object num) {
		if(num == null) return "0";
		String value = String.valueOf(num);
		if(value.contains(".") && value.length() >= 8) {
			value = value.substring(0,value.indexOf(".")+7);
		}
		return value;
	}
	
	public static String getNoRepetitionNum(Integer length) {
		//格式化当前时间
        SimpleDateFormat sfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String strDate = sfDate.format(new Date());
        String random = getRandom(length);
        return strDate + random;
	}
	
    public static String getRandom(Integer length) {
        String result = "";
        Random rand = new Random();
        int n = 20;
        if (null != length && length > 0) {
            n = length;
        }
        int randInt = 0;
        for (int i = 0; i < n; i++) {
            randInt = rand.nextInt(10);
 
            result += randInt;
        }
        return result;
    }
    
    
    /**
     * 判断一个字符串是否都为数字
     * @param strNum
     * @return
     */
    public static boolean isNum(String strNum) {  
        Pattern pattern = Pattern.compile("[0-9]{1,}");  
        Matcher matcher = pattern.matcher((CharSequence) strNum);  
        return matcher.matches();  
    }
    
    /**
     * 判断一个字符串是否都为数字(包含负数)
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("-?[0-9]+(.[0-9]+)?");
	    Matcher isNum = pattern.matcher(str);
	    if( !isNum.matches() ){
	        return false;
	    }
	    return true;
	}
    public static String sqlPar(String par, String sqlCol) {
		String sqlQuery = "";
		if (StringUtils.isNotBlank(par)) {
			sqlQuery = " and " + sqlCol + "='" + par + "'";
		}
		return sqlQuery;
	}
    
    public static String sqlNotPar(String par, String sqlCol) {
		String sqlQuery = "";
		if (StringUtils.isNotBlank(par)) {
			sqlQuery = " and " + sqlCol + "!='" + par + "'";
		}
		return sqlQuery;
	}
    /**
     * 模糊查询
     *
     * @param par
     * @param sqlCol
     * @return
     */
    public static String sqlFuzzyPar(String par, String sqlCol) {
        String sqlQuery = "";
        if (StringUtils.isNotBlank(par)) {
            sqlQuery = " and " + sqlCol + " like '%" + par + "%'";
        }
        return sqlQuery;
    }
	
	public static String sqlParDate(String date1,String date2,String sqlCol) {
		String sqlQuery = "";
		if (StringUtils.isNotBlank(date1)) {
			sqlQuery+= " and " + sqlCol + ">=to_date('" + date1 + "','yyyy-mm-dd')";
		}
		if (StringUtils.isNotBlank(date2)) {
		    date2 = formateDate(date2);
			sqlQuery+= " and " + sqlCol + "<=to_date('" + date2 + "','yyyy-mm-dd')";
		}
		return sqlQuery;
	}
	
	/**
     * 格式化查询条件endtime，day+1
     *
     * @param args
     * @return
     */
    public static String formateDate(String strDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String strDay = "";
            if(day<10) {
                strDay = "0" + day;
            }else {
                strDay = "" + day;
            }
            
            return year+"-"+month+"-"+strDay;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Collection<? extends GrantedAuthority> getGrantedAuthorities(){
		SecurityContext context =SecurityContextHolder.getContext();
		if(context!=null){
			Authentication authentication =context.getAuthentication();
			if(authentication!=null){
				return authentication.getAuthorities();
			}
		}
		return null;
	}
    public static List<String> getAllRoleIds(){
		Collection<? extends GrantedAuthority> authorities =getGrantedAuthorities();
		if(authorities!=null && authorities.size()>0){
			List<String> result =new ArrayList<String>();
			for(GrantedAuthority authority : authorities) {
				if(authority instanceof SecurityRole) {
					SecurityRole role =(SecurityRole)authority;
					result.add(role.getCode());
				}
			}
			return result;
		}
		return null;
	}
    
}
