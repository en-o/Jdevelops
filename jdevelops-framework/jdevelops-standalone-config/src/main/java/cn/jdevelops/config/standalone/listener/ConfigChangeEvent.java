package cn.jdevelops.config.standalone.listener;

import cn.jdevelops.config.standalone.properties.ConfigMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 属性变化事件
 * @author tan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigChangeEvent {
    ConfigMeta meta;
    Map<String, String> config;
}
