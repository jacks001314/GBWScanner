package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import org.apache.hadoop.yarn.api.records.ResourceInformation;
import org.apache.hadoop.yarn.util.UnitsConversionUtil;
import org.apache.hadoop.yarn.util.resource.ResourceUtils;

import java.util.HashMap;
import java.util.Map;

public class GBWYarnUtils {

    public static Map<String, Long> parseResourcesString(String resourcesStr) {
        Map<String, Long> resources = new HashMap<>();

        // Ignore the grouping "[]"
        if (resourcesStr.startsWith("[")) {
            resourcesStr = resourcesStr.substring(1);
        }
        if (resourcesStr.endsWith("]")) {
            resourcesStr = resourcesStr.substring(0, resourcesStr.length());
        }

        for (String resource : resourcesStr.trim().split(",")) {
            resource = resource.trim();
            if (!resource.matches("^[^=]+=\\d+\\s?\\w*$")) {
                throw new IllegalArgumentException("\"" + resource + "\" is not a " +
                        "valid resource type/amount pair. " +
                        "Please provide key=amount pairs separated by commas.");
            }
            String[] splits = resource.split("=");
            String key = splits[0], value = splits[1];
            String units = ResourceUtils.getUnits(value);
            String valueWithoutUnit = value.substring(
                    0, value.length() - units.length()).trim();
            Long resourceValue = Long.valueOf(valueWithoutUnit);
            if (!units.isEmpty()) {
                resourceValue = UnitsConversionUtil.convert(units, "Mi", resourceValue);
            }
            if (key.equals("memory")) {
                key = ResourceInformation.MEMORY_URI;
            }
            resources.put(key, resourceValue);
        }
        return resources;
    }


}
