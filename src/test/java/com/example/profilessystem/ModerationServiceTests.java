package com.example.profilessystem;
import com.example.profilessystem.mapper.ModerationMapper;
import com.example.profilessystem.server.ModerationService;
import com.example.profilessystem.config.ModerationAdvice;
import com.example.profilessystem.entity.ChatBody;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;
class ModerationServiceTests {
 private ModerationMapper mapper(){ModerationMapper m=mock(ModerationMapper.class);when(m.all()).thenReturn(List.of(Map.of("id",1L,"word","测试违规"),Map.of("id",2L,"word","badword")));return m;}
 @Test void requiresAdminSessionForEveryWordOperation(){
  var service=mock(ModerationService.class);
  var controller=new com.example.profilessystem.controller.ModerationController(service);
  var session=new org.springframework.mock.web.MockHttpSession();
  assertEquals(401,controller.all(session).getCode());
  assertEquals(401,controller.add(new com.example.profilessystem.controller.ModerationController.AddWords("test"),session).getCode());
  assertEquals(401,controller.delete(1,session).getCode());verifyNoInteractions(service);
 }
 @Test void rejectsObfuscationAndAllowsCleanText(){
  var service=new ModerationService(mapper());
  for(String text:List.of("测试违规","测 试，违\u200b规","ＢＡＤＷＯＲＤ","BaD-Word","bad&#119;ord","bad&amp;#119;ord"))assertThrows(IllegalArgumentException.class,()->service.check(text),text);
  assertDoesNotThrow(()->service.check("学习 Java，今天一起讨论算法。"));
  assertThrows(IllegalArgumentException.class,()->service.check("a".repeat(20001)));
 }
 @Test void blocksWhenDatabaseFailsOrDictionaryEmpty(){
  var m=mock(ModerationMapper.class);when(m.all()).thenThrow(new RuntimeException("offline"));
  assertThrows(ModerationService.ModerationUnavailable.class,()->new ModerationService(m).check("hello"));
  doReturn(List.of()).when(m).all();assertThrows(ModerationService.ModerationUnavailable.class,()->new ModerationService(m).check("hello"));
 }
 @Test void scansChatBodyBeforeControllerRuns(){
  var advice=new ModerationAdvice(new ModerationService(mapper()));ChatBody b=new ChatBody();b.setContent("测试违规");
  assertThrows(IllegalArgumentException.class,()->advice.afterBodyRead(b,null,null,null,null));
 }
 @Test void batchesLinesAndInvalidatesAfterDeletion(){
  var m=mapper();var service=new ModerationService(m);
  when(m.add(anyString(),anyString())).thenReturn(1);
  assertEquals(2,service.add("新词一\n新词二"));
  verify(m).add("新词一","新词一");verify(m).add("新词二","新词二");
  assertThrows(IllegalArgumentException.class,()->service.add("a"));
  service.words();when(m.delete(1)).thenReturn(1);assertEquals(1,service.delete(1));
  when(m.all()).thenReturn(List.of(Map.of("id",2L,"word","badword")));
  assertDoesNotThrow(()->service.check("测试违规"));
 }
}
