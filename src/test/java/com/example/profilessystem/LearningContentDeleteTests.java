package com.example.profilessystem;
import com.example.profilessystem.server.LearningAccessAdminService;
import com.example.profilessystem.controller.LearningAccessAdminController;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class LearningContentDeleteTests {
 @Test void rejectsUnknownTypeWithoutQueryingDatabase(){
  var jdbc=mock(JdbcTemplate.class);var service=new LearningAccessAdminService(jdbc);
  assertThrows(IllegalArgumentException.class,()->service.delete("user",7));verifyNoInteractions(jdbc);
 }
 @Test void rejectsParentWithDependencies(){
  var jdbc=mock(JdbcTemplate.class);var service=new LearningAccessAdminService(jdbc);
  when(jdbc.queryForList(anyString(),eq(Long.class),eq(7L))).thenReturn(List.of(7L));
  when(jdbc.queryForObject(anyString(),eq(Long.class),any(Object[].class))).thenReturn(1L);
  for(String type:List.of("category","major","route"))assertThrows(IllegalArgumentException.class,()->service.delete(type,7));
  verify(jdbc,never()).update(startsWith("DELETE"),any(Object[].class));
 }
 @Test void deletesOnlyRequestedLeaf(){
  var jdbc=mock(JdbcTemplate.class);var service=new LearningAccessAdminService(jdbc);
  when(jdbc.queryForList(anyString(),eq(Long.class),eq(7L))).thenReturn(List.of(7L));
  when(jdbc.update("DELETE FROM learning_resource WHERE id=?",7L)).thenReturn(1);
  assertDoesNotThrow(()->service.delete("resource",7));
  verify(jdbc).update("DELETE FROM learning_resource WHERE id=?",7L);
 }
 @Test void refusesMissingRecord(){
  var jdbc=mock(JdbcTemplate.class);var service=new LearningAccessAdminService(jdbc);
  when(jdbc.queryForList(anyString(),eq(Long.class),eq(7L))).thenReturn(List.of());
  assertThrows(IllegalArgumentException.class,()->service.delete("resource",7));
  verify(jdbc,never()).update(startsWith("DELETE"),any(Object[].class));
 }
 @Test void requiresAdminAtController(){
  var service=mock(LearningAccessAdminService.class);
  assertEquals(401,new LearningAccessAdminController(service).delete("resource",7,new MockHttpSession()).getCode());
  verifyNoInteractions(service);
 }
}
