package com.example.profilessystem.config;
import com.example.profilessystem.server.ModerationService;
import com.example.profilessystem.result.Result;
import org.springframework.core.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.beans.BeanWrapperImpl;
import java.lang.reflect.Type;
import java.util.*;
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ModerationAdvice extends RequestBodyAdviceAdapter {
 private final ModerationService service;
 private static final Set<String> FIELDS=Set.of("content","message","reason","name","username","description","title","recoveryReason");
 public ModerationAdvice(ModerationService service){this.service=service;}
 @Override public boolean supports(MethodParameter parameter,Type type,Class<? extends HttpMessageConverter<?>> converter){
  String controller=parameter.getContainingClass().getSimpleName();
  return !Set.of("ModerationController","ManagerController").contains(controller)
    && !(parameter.getMethod()!=null && parameter.getMethod().getName().startsWith("login"));
 }
 @Override public Object afterBodyRead(Object body,HttpInputMessage input,MethodParameter p,Type type,Class<? extends HttpMessageConverter<?>> converter){
  scan(body,null,0,new int[]{0});return body;
 }
 private void scan(Object value,String field,int depth,int[] budget){
  if(value==null)return;
  if(depth>16||++budget[0]>4096)throw new IllegalArgumentException("提交内容过于复杂");
  if(value instanceof String text){if(FIELDS.contains(field==null?"":field))service.check(text);return;}
  if(value instanceof Map<?,?> map){for(var e:map.entrySet())scan(e.getValue(),String.valueOf(e.getKey()),depth+1,budget);return;}
  if(value instanceof Iterable<?> list){for(var v:list)scan(v,field,depth+1,budget);return;}
  if(!value.getClass().getPackageName().startsWith("com.example.profilessystem.entity"))return;
  var bean=new BeanWrapperImpl(value);
  for(var property:bean.getPropertyDescriptors())if(!property.getName().equals("class")&&bean.isReadableProperty(property.getName()))scan(bean.getPropertyValue(property.getName()),property.getName(),depth+1,budget);
 }
 @ExceptionHandler(ModerationService.ModerationUnavailable.class)
 public ResponseEntity<Result<?>> unavailable(){return ResponseEntity.status(503).body(Result.fail(503,"内容审核暂不可用，请联系管理员完善词库后重试"));}
 @ExceptionHandler(IllegalArgumentException.class)
 public ResponseEntity<Result<?>> invalid(IllegalArgumentException e){return ResponseEntity.badRequest().body(Result.fail(400,e.getMessage()));}
}
