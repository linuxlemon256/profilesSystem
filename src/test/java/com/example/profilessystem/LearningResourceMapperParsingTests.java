package com.example.profilessystem;
import com.example.profilessystem.mapper.LearningResourceMapper;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
class LearningResourceMapperParsingTests {
 private Configuration config(){var c=new Configuration();c.setMapUnderscoreToCamelCase(true);c.addMapper(LearningResourceMapper.class);return c;}
 @Test void actualMapperCanRegisterWithoutXmlErrors(){assertDoesNotThrow(this::config);}
 @Test void queryWithFiltersKeepsRiskAndLevelChecks(){
  var c=config();var p=new HashMap<String,Object>();p.put("categorySlug","computer");p.put("majorCode","080717T");p.put("query","课程");p.put("level",1);
  var bound=c.getMappedStatement(LearningResourceMapper.class.getName()+".findVisible").getBoundSql(p);
  String sql=bound.getSql();assertTrue(sql.contains("UNVERIFIED"));assertTrue(sql.contains("!='RISK'"));assertTrue(sql.contains("r.required_level<="));assertTrue(sql.contains("c.slug="));assertEquals(5,bound.getParameterMappings().size());
 }
 @Test void emptyFiltersStillBuildAValidProtectedQuery(){
  var c=config();var p=new HashMap<String,Object>();p.put("level",0);
  var bound=c.getMappedStatement(LearningResourceMapper.class.getName()+".findVisible").getBoundSql(p);
  assertEquals(1,bound.getParameterMappings().size());assertTrue(bound.getSql().contains("!='RISK'"));assertFalse(bound.getSql().contains("&lt;"));
 }
 @Test void allApplicationMappersCanRegister(){
  var c=new Configuration();c.setMapUnderscoreToCamelCase(true);
  assertDoesNotThrow(()->c.addMappers("com.example.profilessystem.mapper"));
 }
}
