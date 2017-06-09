package command;

import ua.com.pandasushi.Config;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Taras on 31-Mar-17.
 */
public class Command implements Serializable {
    static final long serialVersionUID = 42L;

    Config config;
    String method;
    Class[] argumentTypes;
    Object[] arguments;

    public Command() {

    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class[] getArgumentTypes() {
        return argumentTypes;
    }

    public void setArgumentTypes(Class[] argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Command command = (Command) o;

        if (method != null ? !method.equals(command.method) : command.method != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(argumentTypes, command.argumentTypes)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(arguments, command.arguments);
    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(argumentTypes);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }

    @Override
    public String toString() {
        return "command.Command{" +
                "method='" + method + '\'' +
                ", argumentTypes=" + Arrays.toString(argumentTypes) +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }
}
