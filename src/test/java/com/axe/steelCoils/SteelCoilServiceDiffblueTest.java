package com.axe.steelCoils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.axe.colors.Color;
import com.axe.finishes.Finish;
import com.axe.gauges.Gauge;
import com.axe.grvs.services.CalculateCostPerMtrService;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.warehouse.Warehouse;
import com.axe.width.Width;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SteelCoilService.class})
@ExtendWith(SpringExtension.class)
class SteelCoilServiceDiffblueTest {
    @MockBean
    private CalculateCostPerMtrService calculateCostPerMtrService;

    @MockBean
    private SteelCoilRepository steelCoilRepository;

    @Autowired
    private SteelCoilService steelCoilService;

    /**
     * Test
     * {@link SteelCoilService#createNewSteelCoil(String, String, BigDecimal, BigDecimal, String, SteelSpecification, Warehouse, BigDecimal, BigDecimal)}.
     * <p>
     * Method under test:
     * {@link SteelCoilService#createNewSteelCoil(String, String, BigDecimal, BigDecimal, String, SteelSpecification, Warehouse, BigDecimal, BigDecimal)}
     */
    @Test
    @DisplayName("Test createNewSteelCoil(String, String, BigDecimal, BigDecimal, String, SteelSpecification, Warehouse, BigDecimal, BigDecimal)")
    @Disabled("TODO: Complete this test")
    void testCreateNewSteelCoil() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext for [MergedContextConfiguration@1998099c testClass = com.axe.steelCoils.DiffblueFakeClass0, locations = [], classes = [com.axe.steelCoils.SteelCoilService], contextInitializerClasses = [], activeProfiles = [], propertySourceLocations = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@645842e8, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@53bc00eb, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@6dc04d2e, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@9da1, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@9ec3328], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:142)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:127)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1708)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   org.mockito.exceptions.base.MockitoException:
        //   Mockito cannot mock this class: class com.axe.grvs.services.CalculateCostPerMtrService.
        //   If you're not sure why you're getting this error, please open an issue on GitHub.
        //   Java               : 21
        //   JVM vendor name    : Amazon.com Inc.
        //   JVM vendor version : 21.0.5+11-LTS
        //   JVM name           : OpenJDK 64-Bit Server VM
        //   JVM version        : 21.0.5+11-LTS
        //   JVM info           : mixed mode, sharing
        //   OS name            : Mac OS X
        //   OS version         : 13.2.1
        //   You are seeing this disclaimer because Mockito is configured to create inlined mocks.
        //   You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.
        //   Underlying exception : org.mockito.exceptions.base.MockitoException: Could not modify all classes [class java.lang.Object, class com.axe.grvs.services.CalculateCostPerMtrService]
        //       at org.springframework.boot.test.mock.mockito.MockDefinition.createMock(MockDefinition.java:158)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.registerMock(MockitoPostProcessor.java:185)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.register(MockitoPostProcessor.java:167)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.postProcessBeanFactory(MockitoPostProcessor.java:141)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.postProcessBeanFactory(MockitoPostProcessor.java:129)
        //       at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:358)
        //       at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:194)
        //       at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:747)
        //       at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:565)
        //       at org.springframework.test.context.support.AbstractGenericContextLoader.loadContext(AbstractGenericContextLoader.java:221)
        //       at org.springframework.test.context.support.AbstractGenericContextLoader.loadContext(AbstractGenericContextLoader.java:110)
        //       at org.springframework.test.context.support.AbstractDelegatingSmartContextLoader.loadContext(AbstractDelegatingSmartContextLoader.java:212)
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:184)
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:118)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:127)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1708)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   org.mockito.exceptions.base.MockitoException: Could not modify all classes [class java.lang.Object, class com.axe.grvs.services.CalculateCostPerMtrService]
        //       at net.bytebuddy.TypeCache.findOrInsert(TypeCache.java:168)
        //       at net.bytebuddy.TypeCache$WithInlineExpunction.findOrInsert(TypeCache.java:399)
        //       at net.bytebuddy.TypeCache.findOrInsert(TypeCache.java:190)
        //       at net.bytebuddy.TypeCache$WithInlineExpunction.findOrInsert(TypeCache.java:410)
        //       at org.springframework.boot.test.mock.mockito.MockDefinition.createMock(MockDefinition.java:158)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.registerMock(MockitoPostProcessor.java:185)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.register(MockitoPostProcessor.java:167)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.postProcessBeanFactory(MockitoPostProcessor.java:141)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.postProcessBeanFactory(MockitoPostProcessor.java:129)
        //       at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:358)
        //       at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:194)
        //       at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:747)
        //       at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:565)
        //       at org.springframework.test.context.support.AbstractGenericContextLoader.loadContext(AbstractGenericContextLoader.java:221)
        //       at org.springframework.test.context.support.AbstractGenericContextLoader.loadContext(AbstractGenericContextLoader.java:110)
        //       at org.springframework.test.context.support.AbstractDelegatingSmartContextLoader.loadContext(AbstractDelegatingSmartContextLoader.java:212)
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:184)
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:118)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:127)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1708)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   java.lang.IllegalStateException:
        //   Byte Buddy could not instrument all classes within the mock's type hierarchy
        //   This problem should never occur for javac-compiled classes. This problem has been observed for classes that are:
        //    - Compiled by older versions of scalac
        //    - Classes that are part of the Android distribution
        //       at org.mockito.internal.creation.bytebuddy.InlineBytecodeGenerator.triggerRetransformation(InlineBytecodeGenerator.java:284)
        //       at org.mockito.internal.creation.bytebuddy.InlineBytecodeGenerator.mockClass(InlineBytecodeGenerator.java:217)
        //       at org.mockito.internal.creation.bytebuddy.TypeCachingBytecodeGenerator.lambda$mockClass$0(TypeCachingBytecodeGenerator.java:47)
        //       at net.bytebuddy.TypeCache.findOrInsert(TypeCache.java:168)
        //       at net.bytebuddy.TypeCache$WithInlineExpunction.findOrInsert(TypeCache.java:399)
        //       at net.bytebuddy.TypeCache.findOrInsert(TypeCache.java:190)
        //       at net.bytebuddy.TypeCache$WithInlineExpunction.findOrInsert(TypeCache.java:410)
        //       at org.mockito.internal.creation.bytebuddy.TypeCachingBytecodeGenerator.mockClass(TypeCachingBytecodeGenerator.java:40)
        //       at org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMaker.createMockType(InlineDelegateByteBuddyMockMaker.java:395)
        //       at org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMaker.doCreateMock(InlineDelegateByteBuddyMockMaker.java:355)
        //       at org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMaker.createMock(InlineDelegateByteBuddyMockMaker.java:334)
        //       at org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker.createMock(InlineByteBuddyMockMaker.java:56)
        //       at org.mockito.internal.util.MockUtil.createMock(MockUtil.java:99)
        //       at org.mockito.internal.MockitoCore.mock(MockitoCore.java:88)
        //       at org.mockito.Mockito.mock(Mockito.java:1998)
        //       at org.springframework.boot.test.mock.mockito.MockDefinition.createMock(MockDefinition.java:158)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.registerMock(MockitoPostProcessor.java:185)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.register(MockitoPostProcessor.java:167)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.postProcessBeanFactory(MockitoPostProcessor.java:141)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.postProcessBeanFactory(MockitoPostProcessor.java:129)
        //       at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:358)
        //       at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:194)
        //       at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:747)
        //       at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:565)
        //       at org.springframework.test.context.support.AbstractGenericContextLoader.loadContext(AbstractGenericContextLoader.java:221)
        //       at org.springframework.test.context.support.AbstractGenericContextLoader.loadContext(AbstractGenericContextLoader.java:110)
        //       at org.springframework.test.context.support.AbstractDelegatingSmartContextLoader.loadContext(AbstractDelegatingSmartContextLoader.java:212)
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:184)
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:118)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:127)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1708)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   java.lang.IllegalArgumentException: Java 21 (65) is not supported by the current version of Byte Buddy which officially supports Java 20 (64) - update Byte Buddy or set net.bytebuddy.experimental as a VM property
        //       at net.bytebuddy.utility.OpenedClassReader.of(OpenedClassReader.java:96)
        //       at net.bytebuddy.dynamic.scaffold.TypeWriter$Default$ForInlining.create(TypeWriter.java:4011)
        //       at net.bytebuddy.dynamic.scaffold.TypeWriter$Default.make(TypeWriter.java:2224)
        //       at net.bytebuddy.dynamic.DynamicType$Builder$AbstractBase$UsingTypeWriter.make(DynamicType.java:4050)
        //       at net.bytebuddy.dynamic.DynamicType$Builder$AbstractBase.make(DynamicType.java:3734)
        //       at org.mockito.internal.creation.bytebuddy.InlineBytecodeGenerator.transform(InlineBytecodeGenerator.java:398)
        //       at java.instrument/java.lang.instrument.ClassFileTransformer.transform(ClassFileTransformer.java:244)
        //       at java.instrument/sun.instrument.TransformerManager.transform(TransformerManager.java:188)
        //       at java.instrument/sun.instrument.InstrumentationImpl.transform(InstrumentationImpl.java:610)
        //       at java.instrument/sun.instrument.InstrumentationImpl.retransformClasses0(Native Method)
        //       at java.instrument/sun.instrument.InstrumentationImpl.retransformClasses(InstrumentationImpl.java:225)
        //       at org.mockito.internal.creation.bytebuddy.InlineBytecodeGenerator.triggerRetransformation(InlineBytecodeGenerator.java:280)
        //       at org.mockito.internal.creation.bytebuddy.InlineBytecodeGenerator.mockClass(InlineBytecodeGenerator.java:217)
        //       at org.mockito.internal.creation.bytebuddy.TypeCachingBytecodeGenerator.lambda$mockClass$0(TypeCachingBytecodeGenerator.java:47)
        //       at net.bytebuddy.TypeCache.findOrInsert(TypeCache.java:168)
        //       at net.bytebuddy.TypeCache$WithInlineExpunction.findOrInsert(TypeCache.java:399)
        //       at net.bytebuddy.TypeCache.findOrInsert(TypeCache.java:190)
        //       at net.bytebuddy.TypeCache$WithInlineExpunction.findOrInsert(TypeCache.java:410)
        //       at org.mockito.internal.creation.bytebuddy.TypeCachingBytecodeGenerator.mockClass(TypeCachingBytecodeGenerator.java:40)
        //       at org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMaker.createMockType(InlineDelegateByteBuddyMockMaker.java:395)
        //       at org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMaker.doCreateMock(InlineDelegateByteBuddyMockMaker.java:355)
        //       at org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMaker.createMock(InlineDelegateByteBuddyMockMaker.java:334)
        //       at org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker.createMock(InlineByteBuddyMockMaker.java:56)
        //       at org.mockito.internal.util.MockUtil.createMock(MockUtil.java:99)
        //       at org.mockito.internal.MockitoCore.mock(MockitoCore.java:88)
        //       at org.mockito.Mockito.mock(Mockito.java:1998)
        //       at org.springframework.boot.test.mock.mockito.MockDefinition.createMock(MockDefinition.java:158)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.registerMock(MockitoPostProcessor.java:185)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.register(MockitoPostProcessor.java:167)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.postProcessBeanFactory(MockitoPostProcessor.java:141)
        //       at org.springframework.boot.test.mock.mockito.MockitoPostProcessor.postProcessBeanFactory(MockitoPostProcessor.java:129)
        //       at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:358)
        //       at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:194)
        //       at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:747)
        //       at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:565)
        //       at org.springframework.test.context.support.AbstractGenericContextLoader.loadContext(AbstractGenericContextLoader.java:221)
        //       at org.springframework.test.context.support.AbstractGenericContextLoader.loadContext(AbstractGenericContextLoader.java:110)
        //       at org.springframework.test.context.support.AbstractDelegatingSmartContextLoader.loadContext(AbstractDelegatingSmartContextLoader.java:212)
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:184)
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:118)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:127)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1708)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange
        BigDecimal weightInKgsOnArrival = new BigDecimal("2.3");
        BigDecimal estMeterRunOnArrival = new BigDecimal("2.3");

        Finish finish = new Finish();
        finish.setColors(new ArrayList<>());
        finish.setId(1L);
        finish.setName("Name");

        Color color = new Color();
        color.setColor("Color");
        color.setCssColor("Css Color");
        color.setFinish(finish);
        color.setId(1L);
        color.setProducts(new ArrayList<>());
        color.setSteelSpecifications(new ArrayList<>());

        Gauge gauge = new Gauge();
        gauge.setGauge(new BigDecimal("2.3"));
        gauge.setId(1L);
        gauge.setProducts(new ArrayList<>());
        gauge.setSteelSpecifications(new ArrayList<>());

        Width width = new Width();
        width.setId(1L);
        width.setProducts(new ArrayList<>());
        width.setSteelSpecifications(new ArrayList<>());
        width.setWidth(new BigDecimal("2.3"));

        SteelSpecification steelSpecification = new SteelSpecification();
        steelSpecification.setCoating("Coating");
        steelSpecification.setColor(color);
        steelSpecification.setGauge(gauge);
        steelSpecification.setISQGrade("ISQGrade");
        steelSpecification.setId(1L);
        steelSpecification.setProductsOnPurchaseOrders(new HashSet<>());
        steelSpecification.setSteelCoils(new ArrayList<>());
        steelSpecification.setWidth(width);

        Warehouse warehouse = new Warehouse();
        warehouse.setConsumablesInWarehouses(new ArrayList<>());
        warehouse.setId(1L);
        warehouse.setName("Name");
        warehouse.setSteelCoils(new ArrayList<>());
        BigDecimal conversionRate = new BigDecimal("2.3");

        // Act
        steelCoilService.createNewSteelCoil("42", "42", weightInKgsOnArrival, estMeterRunOnArrival, "Status",
                steelSpecification, warehouse, conversionRate, new BigDecimal("2.3"));
    }

    /**
     * Test
     * {@link SteelCoilService#createNewSteelCoil(String, String, BigDecimal, BigDecimal, String, SteelSpecification, Warehouse, BigDecimal, BigDecimal)}.
     * <ul>
     *   <li>Then return CardNumber is {@code 42}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link SteelCoilService#createNewSteelCoil(String, String, BigDecimal, BigDecimal, String, SteelSpecification, Warehouse, BigDecimal, BigDecimal)}
     */
    @Test
    @DisplayName("Test createNewSteelCoil(String, String, BigDecimal, BigDecimal, String, SteelSpecification, Warehouse, BigDecimal, BigDecimal); then return CardNumber is '42'")
    void testCreateNewSteelCoil_thenReturnCardNumberIs42() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        SteelCoilRepository steelCoilRepository = mock(SteelCoilRepository.class);
        SteelCoilService steelCoilService = new SteelCoilService(steelCoilRepository, new CalculateCostPerMtrService());
        BigDecimal weightInKgsOnArrival = new BigDecimal("2.3");
        BigDecimal estMeterRunOnArrival = new BigDecimal("2.3");

        Finish finish = new Finish();
        finish.setColors(new ArrayList<>());
        finish.setId(1L);
        finish.setName("Name");

        Color color = new Color();
        color.setColor("Color");
        color.setCssColor("Css Color");
        color.setFinish(finish);
        color.setId(1L);
        color.setProducts(new ArrayList<>());
        color.setSteelSpecifications(new ArrayList<>());

        Gauge gauge = new Gauge();
        gauge.setGauge(new BigDecimal("2.3"));
        gauge.setId(1L);
        gauge.setProducts(new ArrayList<>());
        gauge.setSteelSpecifications(new ArrayList<>());

        Width width = new Width();
        width.setId(1L);
        width.setProducts(new ArrayList<>());
        width.setSteelSpecifications(new ArrayList<>());
        width.setWidth(new BigDecimal("2.3"));

        SteelSpecification steelSpecification = new SteelSpecification();
        steelSpecification.setCoating("Coating");
        steelSpecification.setColor(color);
        steelSpecification.setGauge(gauge);
        steelSpecification.setISQGrade("ISQGrade");
        steelSpecification.setId(1L);
        steelSpecification.setProductsOnPurchaseOrders(new HashSet<>());
        steelSpecification.setSteelCoils(new ArrayList<>());
        steelSpecification.setWidth(width);

        Warehouse warehouse = new Warehouse();
        warehouse.setConsumablesInWarehouses(new ArrayList<>());
        warehouse.setId(1L);
        warehouse.setName("Name");
        warehouse.setSteelCoils(new ArrayList<>());
        BigDecimal conversionRate = new BigDecimal("2.3");

        // Act
        SteelCoil actualCreateNewSteelCoilResult = steelCoilService.createNewSteelCoil("42", "42", weightInKgsOnArrival,
                estMeterRunOnArrival, "Status", steelSpecification, warehouse, conversionRate, new BigDecimal("2.3"));

        // Assert
        assertEquals("42", actualCreateNewSteelCoilResult.getCardNumber());
        assertEquals("42", actualCreateNewSteelCoilResult.getCoilNumber());
        assertEquals("Status", actualCreateNewSteelCoilResult.getStatus());
        assertNull(actualCreateNewSteelCoilResult.getConsignor());
        assertNull(actualCreateNewSteelCoilResult.getGrv());
        assertNull(actualCreateNewSteelCoilResult.getSupplier());
        assertNull(actualCreateNewSteelCoilResult.getId());
        assertTrue(actualCreateNewSteelCoilResult.getMachineEvents().isEmpty());
        assertTrue(actualCreateNewSteelCoilResult.getProductTransactions().isEmpty());
        assertTrue(actualCreateNewSteelCoilResult.getSteelCoilInterBranchTransfer().isEmpty());
        assertTrue(actualCreateNewSteelCoilResult.getSteelCoilInterBranchTransferFrom().isEmpty());
        BigDecimal expectedConversionRatio = new BigDecimal("2.3");
        assertEquals(expectedConversionRatio, actualCreateNewSteelCoilResult.getConversionRatio());
        BigDecimal expectedEstimatedMeterRunOnArrival = new BigDecimal("2.3");
        assertEquals(expectedEstimatedMeterRunOnArrival, actualCreateNewSteelCoilResult.getEstimatedMeterRunOnArrival());
        BigDecimal expectedLandedCostPerKg = new BigDecimal("2.3");
        assertEquals(expectedLandedCostPerKg, actualCreateNewSteelCoilResult.getLandedCostPerKg());
        BigDecimal expectedWeightInKgsOnArrival = new BigDecimal("2.3");
        assertEquals(expectedWeightInKgsOnArrival, actualCreateNewSteelCoilResult.getWeightInKgsOnArrival());
        BigDecimal expectedLandedCostPerMtr = new BigDecimal("2.30");
        assertEquals(expectedLandedCostPerMtr, actualCreateNewSteelCoilResult.getLandedCostPerMtr());
        assertSame(steelSpecification, actualCreateNewSteelCoilResult.getSteelSpecification());
        assertSame(warehouse, actualCreateNewSteelCoilResult.getWarehouse());
        assertSame(estMeterRunOnArrival, actualCreateNewSteelCoilResult.getEstimatedMetersRemaining());
    }
}
