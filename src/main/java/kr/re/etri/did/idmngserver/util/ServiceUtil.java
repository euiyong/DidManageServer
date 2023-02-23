package kr.re.etri.did.idmngserver.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.re.etri.did.message.ext.ServiceRootRequest;

public class ServiceUtil {
    public static String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("Proxy-Client-IP");
        if (clientIp == null) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
            if (clientIp == null) {
                clientIp = request.getHeader("X-Forwarded-For");
                if (clientIp == null) {
                    clientIp = request.getRemoteAddr();
                }
            }
        }
        return clientIp;
     }

	
	public static Object generateResponse(ServiceRootRequest request, String HttpStatus, Class<?> clsResp) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Object obj = clsResp.newInstance();
		Method reusltCode = clsResp.getMethod("setResultCode", String.class);
		reusltCode.invoke(obj, HttpStatus);

		Method reusltMsg = clsResp.getMethod("setResultMsg", String.class);
		reusltMsg.invoke(obj, "Success");
		
		Method msgType = clsResp.getMethod("setMsgType", String.class);
		msgType.invoke(obj, obj.getClass().getSimpleName());

		if(request != null) {
			Method operation = clsResp.getMethod("setOperation", String.class);
			operation.invoke(obj, request.getOperation());
		}

		return obj;
	}





	public static String generateLogMessage(String tid, String MessageData) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String servletPath = request.getServletPath();

		return new StringBuilder()
							.append("[")
							.append(servletPath)
							.append("] ")
							.append(tid)
							.append(" - ")
							.append(MessageData)
							.toString();
	}
}
