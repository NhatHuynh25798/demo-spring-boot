package com.demo.dao.aspect;

import com.demo.model.AbstractModel;
import com.demo.utils.SystemUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

@Component
@Aspect
public class AutoPopulateAuditFieldsAspect {
    private static final String AUDIT_FIELD_NAME_DATE_CREATED = "createdAt";
    private static final String AUDIT_FIELD_NAME_DATE_UPDATED = "updatedAt";
    private static final String AUDIT_FIELD_NAME_DISPLAY = "displayToUser";

    @Around("execution(* com.demo.dao.*DAO.merge(..))")
    public Object aspectMethodMerge(ProceedingJoinPoint call) throws Throwable {
        return process(call, "merge");
    }

    @Around("execution(* com.demo.dao.*DAO.save(..))")
    public Object aspectMethodSave(ProceedingJoinPoint call) throws Throwable {
        return process(call, "save");
    }

    public Object process(ProceedingJoinPoint call, String type) throws Throwable {
        Object param = call.getArgs()[0];
        if (param instanceof AbstractModel) {
            AbstractModel abstractModel = (AbstractModel) param;
            buildMandatoryFields(abstractModel, type);
        } else if (param instanceof List) {
            List list = (List) param;
            for (Object obj : list) {
                if (obj instanceof AbstractModel) {
                    AbstractModel abstractModel = (AbstractModel) obj;
                    buildMandatoryFields(abstractModel, type); // do not
                }
            }
        }
        return call.proceed();
    }

    private AbstractModel buildMandatoryFields(AbstractModel baseEntity, String type) {
        Date dateToSet = new Date();
        try {
            if (type.equals("save")) {
                baseEntity.getClass().getMethod(SystemUtils.getInstance()
                        .buildFieldSetter(AUDIT_FIELD_NAME_DATE_UPDATED), Date.class).invoke(baseEntity, dateToSet);
                baseEntity.getClass().getMethod(SystemUtils.getInstance()
                        .buildFieldSetter(AUDIT_FIELD_NAME_DATE_CREATED), Date.class).invoke(baseEntity, dateToSet);
                baseEntity.getClass().getMethod(SystemUtils.getInstance()
                        .buildFieldSetter(AUDIT_FIELD_NAME_DISPLAY), Boolean.class).invoke(baseEntity, true);
            }
            if (type.equals("merge")) {
                if(baseEntity.getCreatedAt() == null) {
                    baseEntity.getClass().getMethod(SystemUtils.getInstance()
                            .buildFieldSetter(AUDIT_FIELD_NAME_DATE_CREATED), Date.class).invoke(baseEntity, dateToSet);
                }
                baseEntity.getClass().getMethod(SystemUtils.getInstance()
                        .buildFieldSetter(AUDIT_FIELD_NAME_DATE_UPDATED), Date.class).invoke(baseEntity, dateToSet);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return baseEntity;
    }
}
