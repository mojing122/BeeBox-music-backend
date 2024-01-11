package live.mojing.beebox.mapper.entity;

import lombok.Data;

@Data
public class ResponseBodyMessage<T> {
    private int status;
    private boolean success;
    private T message;

    private ResponseBodyMessage(int status, boolean success, T message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public static <T> ResponseBodyMessage<T> success(){
        return new ResponseBodyMessage<>(200,true,null);
    }

    public static <T> ResponseBodyMessage<T> success(T data){
        return new ResponseBodyMessage<>(200,true,data);
    }

    public static <T> ResponseBodyMessage<T> failure(int status){
        return new ResponseBodyMessage<>(status,false,null);
    }

    public static <T> ResponseBodyMessage<T> failure(int status, T data){
        return new ResponseBodyMessage<>(status,false,data);
    }
}