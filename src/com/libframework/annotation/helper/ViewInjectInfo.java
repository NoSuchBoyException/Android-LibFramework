package com.libframework.annotation.helper;

public class ViewInjectInfo {
	
    public Object value;
    public int parentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewInjectInfo)) return false;

        ViewInjectInfo that = (ViewInjectInfo) o;

        if (parentId != that.parentId) return false;
        if (value == null) return (null == that.value);

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + parentId;
        return result;
    }
}
