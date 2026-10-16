package com.exambaby.common.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 自动填充处理器
 * 
 * 功能说明：
 * 自动填充实体类中的 createTime 和 updateTime 字段
 * 
 * 工作原理：
 * - 插入操作时：自动设置 createTime 和 updateTime 为当前时间
 * - 更新操作时：自动更新 updateTime 为当前时间
 * 
 * 使用方式：
 * 在实体类字段上添加 @TableField(fill = FieldFill.INSERT) 或 
 * @TableField(fill = FieldFill.INSERT_UPDATE) 注解
 * 
 * 条件加载：
 * 只在 MyBatis Plus 存在时才加载此配置
 * 网关等不需要数据库的模块会自动排除
 */
@Component
@ConditionalOnClass(MetaObjectHandler.class)
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     * 自动设置 createTime 和 updateTime 为当前时间
     * 
     * @param metaObject 元对象，包含实体类信息
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    /**
     * 更新时自动填充
     * 自动更新 updateTime 为当前时间
     * 
     * @param metaObject 元对象，包含实体类信息
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
