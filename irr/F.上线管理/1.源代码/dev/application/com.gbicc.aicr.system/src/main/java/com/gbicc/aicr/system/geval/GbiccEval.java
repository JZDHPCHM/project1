package com.gbicc.aicr.system.geval;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zch
 * 
 */
public class GbiccEval {

	public static final List<String> lc = new ArrayList<String>();// ���������
	static {
		lc.add("+");
		lc.add("`");
		lc.add("*");
		lc.add("/");
	}

	public static final List<String> lj = new ArrayList<String>();// �����߼������
	static {
		lj.add(">");
		lj.add("<");
		lj.add("=");
		lj.add("!");
	}

	public static final Map<String, int[]> funMap = new HashMap<String, int[]>();
	// int����ע��,��һ����ʶ:0�Դ��� 1�Զ��庯��;�ڶ�����ʶ:�������
	static {
		// �Դ���,�����÷������
		funMap.put("abs", new int[] { 0, 1 });
		funMap.put("acos", new int[] { 0, 1 });
		funMap.put("asin", new int[] { 0, 1 });
		funMap.put("atan", new int[] { 0, 1 });
		funMap.put("cbrt", new int[] { 0, 1 });
		funMap.put("ceil", new int[] { 0, 1 });
		funMap.put("cos", new int[] { 0, 1 });
		funMap.put("cosh", new int[] { 0, 1 });
		funMap.put("exp", new int[] { 0, 1 });
		funMap.put("expm1", new int[] { 0, 1 });
		funMap.put("floor", new int[] { 0, 1 });
		funMap.put("log", new int[] { 0, 1 });
		funMap.put("log10", new int[] { 0, 1 });
		funMap.put("log1p", new int[] { 0, 1 });
		funMap.put("random", new int[] { 0, 1 });
		funMap.put("rint", new int[] { 0, 1 });
		funMap.put("round", new int[] { 0, 1 });
		funMap.put("signum", new int[] { 0, 1 });
		funMap.put("sin", new int[] { 0, 1 });
		funMap.put("sinh", new int[] { 0, 1 });
		funMap.put("sqrt", new int[] { 0, 1 });
		funMap.put("tan", new int[] { 0, 1 });
		funMap.put("tanh", new int[] { 0, 1 });
		funMap.put("max", new int[] { 0, 2 });
		funMap.put("min", new int[] { 0, 2 });

		// �Զ��庯��
		funMap.put("if", new int[] { 1, 3 });
		funMap.put("mod", new int[] { 1, 2 });
		funMap.put("int", new int[] { 1, 1 });
	}

	/**
	 * ��ʽ��ʼ��ת��
	 * 
	 * @param str
	 * @return
	 */
	public static String strCast(String str) {
		str = str.toLowerCase();// ȥ��ո񣬱�Сд

		if (str == null ? true : str.length() == 0)
			return "0";
		if (!checkFormula(str))
			return "0";
		str = str.replaceAll("\\*-", "**");
		str = str.replaceAll("-\\*", "**");
		str = str.replaceAll("/-", "//");
		str = str.replaceAll("-/", "//");
		str = str.replaceAll("\\+-", "-");
		str = str.replaceAll("-\\+", "-");
		str = str.replaceAll("-", "`");
		str = str.replaceAll("\\*\\*", "*-");
		str = str.replaceAll("//", "/-");
		str = str.replaceAll(" ", "");
		return str;
	}

	/**
	 * ��鹫ʽ�����ų��ִ����Ƿ���ȷ
	 * 
	 * @param formulaStr
	 * @return
	 */
	public static boolean checkFormula(String formulaStr) {
		boolean flag = true;
		int count = 0;
		for (int i = 0; i < formulaStr.length(); i++) {
			String s = String.valueOf(formulaStr.charAt(i));
			if ("(".equals(s))
				count++;
			else if (")".equals(s))
				count--;
		}
		flag = count == 0;
		return flag;
	}

	/**
	 * �ָ��
	 * 
	 * @param str
	 * @param bs
	 * @return
	 */
	public static String[] spliteFun(String str, String bs) {
		List<String> list = new ArrayList<String>();
		String bds = "";
		int bracket = 0;
		int len = str.length();
		for (int i = 0; i < len; i++) {
			String s = String.valueOf(str.charAt(i));
			if ("(".equals(s)) {
				bracket++;
			} else if (")".equals(s)) {
				bracket--;
			}

			if (bracket == 0 && bs.equals(s)) {
				list.add(bds);
				bds = "";
				continue;
			}

			bds += s;
		}

		list.add(bds);

		String[] ss = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ss[i] = list.get(i);
		}

		return ss;
	}

	/**
	 * �û��Զ��庯��
	 * 
	 * @param str
	 * @param funStr
	 * @return
	 */
	public static String customFun(String str, String funStr) {
		String reval = "0";

		String[] gss = spliteFun(str, ",");
		if ("if".equals(funStr)) {
			if (compare(gss[0])) {
				reval = calculate(gss[1]);
			} else {
				reval = calculate(gss[2]);
			}
		} else if ("mod".equals(funStr)) {
			double d2 = new Double(calculate(gss[1]));
			if (d2 == 0)
				return reval;
			double d1 = new Double(calculate(gss[0]));
			reval = (d1 % d2) + "";
		} else if ("int".equals(funStr)) {
			reval = Math.floor(new Double(calculate(gss[0]))) + "";
		}
		return reval;
	}

	// �߼����ʽ�ж�
	public static boolean compare(String str) {
		boolean flag = false;
		boolean bs = false;
		int len = str.length();
		int bracket = 0;
		String ljbds = "";
		double d_left = 0;
		double d_right = 0;

		for (int i = 0; i < len; i++) {
			String s = String.valueOf(str.charAt(i));
			if ("(".equals(s)) {
				bracket++;
			} else if (")".equals(s)) {
				bracket--;
			}

			if (bracket == 0 && lj.contains(s)) {
				for (int j = i; j < len; j++) {
					String ts = String.valueOf(str.charAt(j));
					if (lj.contains(ts)) {
						ljbds += ts;
					} else {
						bs = true;
						break;
					}
				}
			}
			if (bs)
				break;
		}

		String[] s = str.split(ljbds);
		d_left = new Double(calculate(s[0]));
		d_right = new Double(calculate(s[1]));

		if ("<".equals(ljbds)) {
			if (d_left < d_right)
				return true;
		} else if (">".equals(ljbds)) {
			if (d_left > d_right)
				return true;
		} else if ("=".equals(ljbds)) {
			if (d_left == d_right)
				return true;
		} else if (">=".equals(ljbds)) {
			if (d_left >= d_right)
				return true;
		} else if ("<=".equals(ljbds)) {
			if (d_left <= d_right)
				return true;
		} else if ("<>".equals(ljbds) || "!=".equals(ljbds)) {
			if (d_left != d_right)
				return true;
		}

		return flag;
	}

	/**
	 * �ݹ��������
	 * 
	 * @param str
	 * @return
	 */
	public static String calculate(String str) {

		String reval = "";
		String bds = "";
		int bracket = 0;// ��Ӧ���Ÿ���
		int pos = 0;
		boolean title = false;

		if (str.substring(0, 1).equals("`")) {
			str = str.substring(1);
			title = true;
		}

		int len = str.length();

		for (int i = 0; i < len; i++) {
			String s = String.valueOf(str.charAt(i));
			pos = i;
			bracket = 0;
			if (!lc.contains(s)) {// ���û���������
				if ("(".equals(s)) {// �������������
					if (funMap.containsKey(bds)) {// ���������ǰ�Ǻ���
						for (int j = i + 1; j < len; j++) {// �������ź�ʼѭ��
							pos++;// �ۼ��ƶ��ַ�λ��
							String ts = String.valueOf(str.charAt(j));// �����ַ�
							// reval+=ts;
							if ("(".equals(ts))// ������������ۼ�
								bracket++;
							else if (")".equals(ts)) {// ����������Ž��м���
								bracket--;
								if (bracket == -1) {// �����-1,��ʶ���Ž���
									reval = reval.substring(0, reval.length()
											- bds.length());//���»��ȥ������ͷ�ı��
									// ʽ
									reval += funCalculate(str.substring(i + 1,
											j), bds);// ���ʽ���Ϻ�����,�γ��±��ʽ
									i = pos;// ����������
									bds = "";// ����ͷ���
									break;// �˳�����ѭ��
								}
							}
						}
					} else {// �������ͨ����
						for (int j = i + 1; j < len; j++) {
							pos++;
							String ts = String.valueOf(str.charAt(j));
							if ("(".equals(ts))
								bracket++;
							else if (")".equals(ts)) {
								bracket--;
								if (bracket == -1) {
									reval += calculate(str
											.substring(i + 1, pos));
									i = pos;
									bds = "";
									break;
								}
							}
						}
					}
				} else {// �ۼ��ܱ��ʽ�����һ��������(����)
					bds += s;
					reval += s;
				}
			} else {// ������������һ��������(����)���
				bds = "";
				reval += s;
			}
		}

		if (title)
			reval = "-" + reval;
		return szys(reval);
	}

	/**
	 * ��������
	 * 
	 * @param gs
	 * @param flag
	 * @return
	 */
	public static String funCalculate(String gs, String funStr) {
		String rval = "0";
		if (funMap.containsKey(funStr)) {
			int[] csi = funMap.get(funStr);
			try {
				if (csi[0] == 0) {// java�ڲ�����,ͨ�������
					Class[] cs = new Class[csi[1]];
					Object[] objs = new Object[csi[1]];
					String[] gss = zlcs(gs);
					for (int i = 0; i < csi[1]; i++) {
						cs[i] = double.class;
						objs[i] = new Double(calculate(gss[i]));
					}
					Class cls = Class.forName("java.lang.Math");
					Method m = cls.getMethod("abs", cs);
					rval = String.valueOf(m.invoke(cls, objs));
				} else if (csi[0] == 1) {// �Զ��庯��
					rval = customFun(gs, funStr);
				}
			} catch (Exception e) {

			}
		}

		return rval;
	}

	// ��ʽ��Ĳ���ָ�
	public static String[] zlcs(String str) {
		int len = str.length();
		boolean flag = true;
		String tstr = "";

		for (int i = 0; i < len; i++) {
			String s = String.valueOf(str.charAt(i));
			if ("(".equals(s)) {
				flag = false;
			} else if (")".equals(s)) {
				flag = true;
			}
			if (flag && ",".equals(s)) {
				tstr += "@";
			} else {
				tstr += s;
			}
		}

		return tstr.split("@");

	}

	/**
	 * ����������ʽ����
	 * 
	 * @param str
	 * @return
	 */
	public static String szys(String gs) {
		gs = gs + "+0"; // ��Ϊ����ļ�����������ŲŽ���,���Զ����һ��������,��Ӱ��ֵ.
		String c1 = "";// ��һ��������
		String c2 = "";// �ڶ���������
		String s1 = "";// ��һ��������
		String s2 = "";// �ڶ���������
		String s3 = "";// �����������

		int len = gs.length();
		for (int i = 0; i < len; i++) {
			String s = String.valueOf(gs.charAt(i));// ��ø�λ���ַ�ת�����ַ����Ƚ�

			if (lc.contains(s)) { // �����������
				if (c1.length() == 0)// ����һ��������Ϊ��,����
					c1 = s;
				else if (c2.length() == 0) {// ����,���ڶ���������Ϊ��,����
					c2 = s;// �ڶ���������
					if ("+".equals(c2) || "`".equals(c2)) {//���ڶ��������ż����,�
						// �ô���м���
						s1 = _4zys(s1, c1, s2);// ��һ���͵ڶ��������
						c1 = c2;// ����ڶ��������,����Ϊ��
						c2 = "";
						s2 = "";
					}
				} else {// �����������
					if ("+".equals(s) || "`".equals(s)) {//��������������,������
						// ��
						s2 = _4zys(s2, c2, s3);// ����ڶ������,�������ڶ���
						s1 = _4zys(s1, c1, s2);// �����һ����,��������һ��
						c1 = s;// ���浱ǰ�����,����Ϊ��
						s2 = "";
						c2 = "";
						s3 = "";
					} else {// ��������������
						s2 = _4zys(s2, c2, s3);// ����ڶ������,�������ڶ���
						c2 = s;// ǰ�治��,���������
						s3 = "";
					}
				}
			} else if (s1.length() > 0 && c1.length() > 0 && c2.length() == 0) {// �
				// �
				// �
				// �
				// һ
				// �
				// �
				// �
				// �
				// ,
				// �
				// �
				// һ
				// �
				// �
				// �
				// �
				// �
				// �
				// �
				// �
				// ѱ
				// �
				// �
				// �
				// ,
				// �
				// ڶ
				// �
				// �
				// �
				// �
				// �
				// �
				// �
				// �
				// δ
				// �
				// �
				// �
				// �
				// ,
				// �
				// �
				// �
				// �
				// ڶ
				// �
				// �
				// �
				// �
				// �
				s2 += s;
			} else if (c1.length() == 0) {// ���û�������,�����һ����
				s1 += s;
			} else if (s1.length() > 0 && s2.length() > 0 && c1.length() > 0
					&& c2.length() > 0) {// ����һ��������������,����������
				s3 += s;
			}
		}
		return s1;
	}

	/**
	 * ����������
	 * 
	 * @param c1
	 *            ������1
	 * @param s1
	 *            �����(�Ӽ��˳�)
	 * @param c2
	 *            ������2
	 * @return
	 */
	public static String _4zys(String c1, String s1, String c2) {
		String reval = "0";

		try {
			double ln = Double.valueOf(c1).doubleValue();
			double rn = Double.valueOf(c2).doubleValue();
			if ("+".equals(s1)) {
				return (ln + rn) + "";
			} else if ("`".equals(s1)) {
				return (ln - rn) + "";
			} else if ("*".equals(s1)) {
				return (ln * rn) + "";
			} else if ("/".equals(s1)) {
				if (rn == 0)
					return reval;
				else
					return (ln / rn) + "";
			}
		} catch (Exception e) {
		} finally {
		}

		return reval;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String gs = "if(4 > 3,if( ( 2 - 1*1 ) / ( 0.0001 ) * 3 > 3 , ( 2 - ( 2 - 1 ) / ( 0.0001 ) * 3 ) * 0.8 ,0), ( 2 + ( 3 - 2 ) / ( 0.0001 ) *1 ) * 1)";
		// �������������ý��
		System.out.println(szys(strCast("(13-(-4)*(-5)+5)/2.43")));
		// ����������
		System.out.println("111");
		System.out
				.println(calculate(strCast("33+abs(mod(4,if(( 2 - 1*1 )>3,1,3))-abs(2*4))*5+5")));
		System.out.println("222");

		System.out.println(calculate(strCast("if(0.0*2>=10.0,0,10.0-0.0*2)")));

		System.out.println(calculate(strCast("if(0<2,(10.0-0*5)*0.8,0)")));

		System.out.println(calculate(strCast(gs)));
	}

}
