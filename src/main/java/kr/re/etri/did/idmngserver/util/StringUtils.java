package kr.re.etri.did.idmngserver.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Vector;

public class StringUtils {
	/**
	 * 빈값 체크
	 * 
	 * @param tobetested
	 * @return
	 */
	public static final boolean isEmpty(String tobetested) {
		if (tobetested != null && !tobetested.trim().equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Exception을 String 변환
	 * 
	 * @param e
	 * @return
	 */
	public static String exceptionAsString(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();

		return exceptionAsString;
	}

	/**
	 * 문자열 치환
	 * 
	 * @param in_str
	 * @param in_find
	 * @param in_rep
	 * @return
	 */
	public static String replace(String in_str, String in_find, String in_rep) {
		if (in_str == null)
			return null;
		StringBuffer sb = new StringBuffer(in_str.length());
		int lenFind = in_find.length();
		int pos = 0;

		while (true) {
			int posFind = in_str.indexOf(in_find, pos);
			if (-1 == posFind) {
				sb.append(in_str.substring(pos));
				break;
			}
			sb.append(in_str.substring(pos, posFind)).append(in_rep);
			pos = posFind + lenFind;
		}

		return sb.toString();
	}

	public static String[] split(String strTarget, String strDelim) {
		return split(strTarget, strDelim, true);
	}

	public static String[] split(String strTarget, String strDelim, boolean bContainNull) {
		String[] result = null;
		if (strTarget != null) {
			int index = 0;
			Vector<String> vc = new Vector();
			String strCheck = strTarget;
			while (strCheck.length() != 0) {
				int begin = strCheck.indexOf(strDelim);
				if (begin == -1) {
					vc.add(index, strCheck);
					break;
				}
				int end = begin + strDelim.length();
				if (begin != 0 || (begin == 0 && bContainNull))
					vc.add(index++, (begin == 0) ? null : strCheck.substring(0, begin));
				strCheck = strCheck.substring(end);
				if (strCheck.length() == 0 && bContainNull) {
					vc.add(index, null);
					break;
				}
			}
			if (vc.size() > 0) {
				result = new String[vc.size()];
				vc.copyInto((Object[]) result);
			}
		}
		return result;
	}

	public static ArrayList<String> splitText(String text) {
		ArrayList<String> dataList = new ArrayList<>();
		int maxLength = 200;
		int textLen = text.length();
		int loopCnt = textLen / maxLength + 1;

		for (int i = 0; i < loopCnt; i++) {
			String charsData;
			int lastIndex = (i + 1) * maxLength;

			if (textLen > lastIndex) {
				charsData = text.substring(i * maxLength, lastIndex);
			} else {
				charsData = text.substring(i * maxLength);
			}

			dataList.add(charsData);
		}

		return dataList;
	}
}
