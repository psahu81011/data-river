package com.github.psahu81011.river.service;

import com.github.psahu81011.river.service.impl.NoopEnricher;
import com.github.psahu81011.river.service.impl.PassAllFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TransformerFactoryTest {

  @Before
  public void before() {
    TransformerFactory.builder()
            .filter("river1", new PassAllFilter())
            .enricher("river1", new NoopEnricher())
            .backFiller("river1", Mockito.mock(IBackFiller.class))
            .filter("river2", new PassAllFilter())
            .enricher("river2", new NoopEnricher())
            .backFiller("river2", Mockito.mock(IBackFiller.class))
            .build();
  }

  @After
  public void after() {
    TransformerFactory.INSTANCE = null;
  }

  @Test
  public void enricher() {
    Assert.assertNotNull(TransformerFactory.INSTANCE.enricher("river1"));
    Assert.assertNotNull(TransformerFactory.INSTANCE.enricher("river2"));
    Assert.assertNull(TransformerFactory.INSTANCE.enricher("invalid_river"));
  }

  @Test
  public void filter() {
    Assert.assertNotNull(TransformerFactory.INSTANCE.filter("river1"));
    Assert.assertNotNull(TransformerFactory.INSTANCE.filter("river2"));
    Assert.assertNull(TransformerFactory.INSTANCE.filter("invalid_river"));
  }

  @Test
  public void backFiller() {
    Assert.assertNotNull(TransformerFactory.INSTANCE.backFiller("river1"));
    Assert.assertNotNull(TransformerFactory.INSTANCE.backFiller("river2"));
    Assert.assertNull(TransformerFactory.INSTANCE.backFiller("invalid_river"));
  }

  @Test
  public void getRiverTypes() {
    final java.util.List<String> riverTypes = TransformerFactory.INSTANCE.getRiverTypes();
    Assert.assertNotNull(riverTypes);
    Assert.assertEquals(2, riverTypes.size());
    Assert.assertEquals("river1", riverTypes.get(0));
    Assert.assertEquals("river2", riverTypes.get(1));
  }

}