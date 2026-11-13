package com.example.profilessystem;
import com.example.profilessystem.server.LearningResourceService;
import com.example.profilessystem.server.LearningAccessService;
import com.example.profilessystem.mapper.LearningResourceMapper;
import com.example.profilessystem.entity.LearningResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class LinkResourceTests {
 private LearningResource resource(){var r=new LearningResource();r.setCategoryId(1L);r.setTitle("教材链接");r.setTargetUrl("https://example.com/course");return r;}
 private JdbcTemplate jdbc(){var j=mock(JdbcTemplate.class);when(j.queryForObject(anyString(),eq(Integer.class),any(Object[].class))).thenReturn(1);return j;}
 @Test void validatesAddresses(){
  assertEquals("https://example.com/course",LearningResourceService.validateTarget("https://example.com/course"));
  assertEquals("major-industrial-design.html?x=1",LearningResourceService.validateTarget("major-industrial-design.html?x=1"));
  for(String value:new String[]{"javascript:alert(1)","data:text/html,test","//evil.test/a","../a.html","https://user:pass@example.com","http://example.com","https://example.com/\nfoo"})assertThrows(IllegalArgumentException.class,()->LearningResourceService.validateTarget(value),value);
 }
 @Test void newLinksDefaultToUnverifiedDraft(){
  var m=mock(LearningResourceMapper.class);when(m.insert(any())).thenReturn(1);var r=resource();new LearningResourceService(m,mock(LearningAccessService.class),jdbc()).create(r);
  assertEquals("UNVERIFIED",r.getSafetyStatus());assertEquals("DRAFT",r.getStatus());assertNull(r.getReviewedAt());
 }
 @Test void changingAddressClearsReview(){
  var m=mock(LearningResourceMapper.class);var old=resource();old.setSafetyStatus("VERIFIED");when(m.findByIdForAdmin(1)).thenReturn(old);when(m.update(any())).thenReturn(1);
  var r=resource();r.setTargetUrl("https://example.com/new");r.setSafetyStatus("VERIFIED");r.setReviewNote("previous check");
  assertTrue(new LearningResourceService(m,mock(LearningAccessService.class),jdbc()).update(1,r));assertEquals("UNVERIFIED",r.getSafetyStatus());assertNull(r.getReviewNote());assertNull(r.getReviewedAt());
 }
 @Test void riskyLinksAreClosedAndNeedReason(){
  var m=mock(LearningResourceMapper.class);when(m.insert(any())).thenReturn(1);var svc=new LearningResourceService(m,mock(LearningAccessService.class),jdbc());var r=resource();r.setSafetyStatus("RISK");
  assertThrows(IllegalArgumentException.class,()->svc.create(r));r.setReviewNote("异常跳转");svc.create(r);assertFalse(r.getEnabled());assertNotNull(r.getReviewedAt());
 }
 @Test void rejectsCrossCategoryMajor(){
  var m=mock(LearningResourceMapper.class);var j=jdbc();when(j.queryForObject(anyString(),eq(Integer.class),any(Object[].class))).thenReturn(1,0);var r=resource();r.setMajorId(9L);
  assertThrows(IllegalArgumentException.class,()->new LearningResourceService(m,mock(LearningAccessService.class),j).create(r));verify(m,never()).insert(any());
 }
}
