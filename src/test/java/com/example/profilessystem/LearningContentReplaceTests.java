package com.example.profilessystem;
import com.example.profilessystem.server.LearningContentTransferService;
import com.example.profilessystem.entity.LearningContentTransfer;
import com.example.profilessystem.controller.LearningContentTransferController;
import com.example.profilessystem.auth.SessionKeys;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class LearningContentReplaceTests {
 private LearningContentTransfer content(){return new LearningContentTransfer(LearningContentTransfer.FORMAT,1,null,List.of(new LearningContentTransfer.CategoryItem("test","测试",null,null,null,false,true,0)),List.of(),List.of(),List.of());}
 @Test void rejectsIncompleteAndEmptyBeforeAnyDelete(){
  var jdbc=mock(JdbcTemplate.class);var service=new LearningContentTransferService(jdbc);
  assertThrows(IllegalArgumentException.class,()->service.replaceContent(null));
  assertThrows(IllegalArgumentException.class,()->service.replaceContent(new LearningContentTransfer(LearningContentTransfer.FORMAT,1,null,List.of(),List.of(),List.of(),List.of())));
  verifyNoInteractions(jdbc);
 }
 @Test void deletesChildrenBeforeParentsThenImports(){
  var jdbc=mock(JdbcTemplate.class);var service=new LearningContentTransferService(jdbc);
  when(jdbc.queryForObject(anyString(),eq(Integer.class))).thenReturn(4);
  var summary=service.replaceContent(content());assertEquals(1,summary.professionalCategories());
  var order=inOrder(jdbc);order.verify(jdbc).update("DELETE FROM learning_resource");order.verify(jdbc).update("DELETE FROM learning_route");order.verify(jdbc).update("DELETE FROM major");order.verify(jdbc).update("DELETE FROM major_category");
  order.verify(jdbc).update(startsWith("INSERT INTO major_category"),any(Object[].class));
 }
 @Test void writeFailureTriggersTransactionRollback(){
  var jdbc=mock(JdbcTemplate.class);when(jdbc.update(startsWith("INSERT INTO major_category"),any(Object[].class))).thenThrow(new IllegalArgumentException("invalid input"));
  when(jdbc.queryForObject(anyString(),eq(Integer.class))).thenReturn(4);
  var manager=mock(PlatformTransactionManager.class);var status=new SimpleTransactionStatus();when(manager.getTransaction(any())).thenReturn(status);
  var advice=new TransactionInterceptor();advice.setTransactionManager(manager);advice.setTransactionAttributeSource(new AnnotationTransactionAttributeSource());
  var factory=new ProxyFactory(new LearningContentTransferService(jdbc));factory.addAdvice(advice);
  var service=(LearningContentTransferService)factory.getProxy();
  assertThrows(IllegalArgumentException.class,()->service.replaceContent(content()));
  verify(manager).rollback(status);verify(manager,never()).commit(any());
 }
 @Test void rejectsNonTransactionalTablesBeforeDeletion(){
  var jdbc=mock(JdbcTemplate.class);when(jdbc.queryForObject(anyString(),eq(Integer.class))).thenReturn(3);
  assertThrows(IllegalArgumentException.class,()->new LearningContentTransferService(jdbc).replaceContent(content()));
  verify(jdbc,never()).update(startsWith("DELETE"));
 }
 @Test void requiresManagerAndExplicitConfirmation(){
  var service=mock(LearningContentTransferService.class);var controller=new LearningContentTransferController(service);var session=new MockHttpSession();
  assertEquals(401,controller.replaceContent(content(),true,session).getCode());session.setAttribute(SessionKeys.MANAGER,"admin");
  assertEquals(400,controller.replaceContent(content(),false,session).getCode());verifyNoInteractions(service);
 }
}
