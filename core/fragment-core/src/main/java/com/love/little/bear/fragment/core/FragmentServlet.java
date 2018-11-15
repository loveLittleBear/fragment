package com.love.little.bear.fragment.core;

import com.alibaba.fastjson.JSON;
import com.love.little.bear.fragment.core.annotation.ExportService;
import com.love.little.bear.fragment.core.domain.ClassInfo;
import com.love.little.bear.fragment.core.domain.MethodInfo;
import com.love.little.bear.fragment.core.service.TaskService;
import com.love.little.bear.fragment.core.utils.ClazzUtils;
import com.love.little.bear.fragment.core.utils.JsonFormatUtils;
import com.love.little.bear.fragment.core.utils.MethodInfoPredicate;
import com.love.little.bear.fragment.core.domain.ParamInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by hanyang1 on 2017/10/19.
 */
public class FragmentServlet extends HttpServlet {

    private ApplicationContext applicationContext;

    /**
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        //获取servlet 上下文
        ServletContext servletContext = config.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        double version = NumberUtils.toDouble(Servlet.class.getPackage().getSpecificationVersion());
        if (version >= 3) {

        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/json;charset=utf-8");
            String method = request.getParameter("method");
            if ("invoke".equals(method)) {
                Object obj = invoke(request, response);
                String json = JSON.toJSONString(obj);
                response.getWriter().write(json);
                response.getWriter().flush();
            } else if ("services".equals(method)) {
                TaskService taskService = new TaskService(applicationContext);
                List<ClassInfo> classInfos = taskService.getClassInfos(ExportService.class);
                String json = JSON.toJSONString(classInfos);
                response.getWriter().write(json);
                response.getWriter().flush();
            } else if ("index".equals(method)) {
                velocityInit(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object invoke(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //类名和方法名
        String serviceName = request.getParameter("serviceName");
        String methodName = request.getParameter("methodName");
        if (StringUtils.isEmpty(serviceName) || StringUtils.isEmpty(methodName)) {
            return null;
        }
        //根据类名获取bean
        Object serviceObj = applicationContext.getBean(serviceName);
        if (serviceObj == null) {
            return null;
        }
        Class<?> clazz = serviceObj.getClass();
        //根据类名获取classInfo
        ClassInfo classInfo = ClazzUtils.getClassInfo(serviceName, serviceObj.getClass());
        //获取方法列表
        List<MethodInfo> methodInfoList = classInfo.getMethodInfoList();
        //根据入参过滤方法
        MethodInfoPredicate methodInfoPredicate = new MethodInfoPredicate(methodName);
        MethodInfo methodInfo = methodInfoPredicate.select(methodInfoList);
        //方法所有入参信息
        List<ParamInfo> paramInfoList = methodInfo.getParamInfoList();
        Object object = null;
        if (CollectionUtils.isEmpty(paramInfoList)) {//没有入参
            object = clazz.getMethod(methodInfo.getMethodName()).invoke(serviceObj);
        } else {
            Class<?>[] paramTypes = new Class<?>[paramInfoList.size()];
            Object[] paramValue = new Object[paramInfoList.size()];
            for (int i = 0; i < paramInfoList.size(); i++) {
                Class<?> paramType = paramInfoList.get(i).getParamType();
                String paramName = paramInfoList.get(i).getParamName();
                paramTypes[i] = paramType;
                String paramVal = request.getParameter(paramName);
                paramValue[i] = ClazzUtils.convertObj(paramVal, paramInfoList.get(i));
            }
            object = clazz.getMethod(methodInfo.getMethodName(), paramTypes).invoke(serviceObj, paramValue);
        }
        return object;
    }

    public void velocityInit(HttpServletRequest request, HttpServletResponse response) {

    }

    public static void main(String[] args) {
        String version = Servlet.class.getPackage().getSpecificationVersion();
        System.out.println(version);
        System.out.println(Servlet.class.getPackage());
    }
}
