package com.example.profilessystem;
import com.example.profilessystem.server.LearningCatalogService;
import com.example.profilessystem.server.LearningAccessService;
import com.example.profilessystem.mapper.LearningCatalogMapper;
import com.example.profilessystem.entity.MajorCategory;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class MajorCompletionTests {
 @Test void countsActualKnowledgePagesWithinEachCategory(){
  var mapper=mock(LearningCatalogMapper.class);var mechanical=new MajorCategory();mechanical.setId(2L);var electronics=new MajorCategory();electronics.setId(3L);var empty=new MajorCategory();empty.setId(4L);
  when(mapper.findCategories()).thenReturn(List.of(mechanical,electronics,empty));
  when(mapper.findMajorCoverage()).thenReturn(List.of(new LearningCatalogMapper.MajorCoverage(2,"080202"),new LearningCatalogMapper.MajorCoverage(2,"080205"),new LearningCatalogMapper.MajorCoverage(2,"080201"),new LearningCatalogMapper.MajorCoverage(3,"080701")));
  var service=new LearningCatalogService(mapper,mock(LearningAccessService.class));service.categories();
  assertEquals(2,mechanical.getCompletedMajorCount());assertEquals(3,mechanical.getTotalMajorCount());
  assertEquals(1,electronics.getCompletedMajorCount());assertEquals(1,electronics.getTotalMajorCount());
  assertEquals(0,empty.getCompletedMajorCount());assertEquals(0,empty.getTotalMajorCount());
  verify(mapper,times(1)).findMajorCoverage();
 }
 @Test void includesNewUnfinishedMajorsInTotal(){
  var mapper=mock(LearningCatalogMapper.class);var category=new MajorCategory();category.setId(1L);
  when(mapper.findCategories()).thenReturn(List.of(category));when(mapper.findMajorCoverage()).thenReturn(List.of(new LearningCatalogMapper.MajorCoverage(1,"080901"),new LearningCatalogMapper.MajorCoverage(1,"080902")));
  new LearningCatalogService(mapper,mock(LearningAccessService.class)).categories();
  assertEquals(0,category.getCompletedMajorCount());assertEquals(2,category.getTotalMajorCount());
 }
 @Test void artificialIntelligenceCountsAsCompletedForBothCodeFormats(){
  for(String code:List.of("080717T","080717")){
   var mapper=mock(LearningCatalogMapper.class);var category=new MajorCategory();category.setId(3L);
   when(mapper.findCategories()).thenReturn(List.of(category));
   when(mapper.findMajorCoverage()).thenReturn(List.of(new LearningCatalogMapper.MajorCoverage(3,code),new LearningCatalogMapper.MajorCoverage(3,"080703")));
   new LearningCatalogService(mapper,mock(LearningAccessService.class)).categories();
   assertEquals(1,category.getCompletedMajorCount());assertEquals(2,category.getTotalMajorCount());
  }
 }
}
