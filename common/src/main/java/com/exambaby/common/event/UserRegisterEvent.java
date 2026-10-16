package com.exambaby.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户注册事件 DTO (存放在 common 模块，供所有服务引用)
 *
 *   🚀 RocketMQ 集成“黄金公式”
 *
 *   1. 生产者 (Producer) —— “发信人”
 *    * 配置文件：定义一个输出绑定名（比如 user-out-0），并将 destination 指向 Topic（比如 user-register-topic）。
 *    * 代码实现：在业务类中注入 StreamBridge，调用 send("输出绑定名", 消息体)。
 *
 *   2. 消费者 (Consumer) —— “收信人”
 *    * 配置文件 (核心三要素)：
 *        1. 注册函数：在 spring.cloud.stream.function.definition 中写上你的函数名（多个用分号 ; 隔开）。
 *        2. 绑定通道：新建一个输入绑定，名字必须是 函数名-in-0。
 *        3. 对齐 Topic：将该绑定的 destination 设置为跟生产者一致的 Topic。
 *        4. 设置组名：增加 group 属性（比如 user-group），确保消息能被持久化且不漏掉。
 *    * 代码实现：
 *        1. 在一个 @Configuration 类里写一个 @Bean。
 *        2. 方法名必须等于你上面定义的那个函数名。
 *        3. 返回类型必须是 Consumer<你的实体类>。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterEvent implements Serializable {
    private Long userId;
    private String username;
    private String email;
    private LocalDateTime registerTime;
}
