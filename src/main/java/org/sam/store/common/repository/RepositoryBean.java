package org.sam.store.common.repository;

import org.sam.store.product.Product;
import org.sam.store.product.ProductRepository;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Proxy;
import java.util.Set;

@Configuration
public class RepositoryBean {

    //TODO: 추후 자동 스캔 빈등록 추가
//    @Bean
//    public ProductRepository orderRepository() {
//        ProxyRepository<Product, String> defaultMemoryRepository = new ProxyRepository<>();
//        ProductRepository productRepository = (ProductRepository) Proxy.newProxyInstance(
//                Repository.class.getClassLoader(),
//                new Class[] {ProductRepository.class},
//                defaultMemoryRepository
//        );
//
//        return productRepository;
//    }
//
//    @Bean
//    public ProxyRepositoryBeanFactory proxyRepositoryBeanFactory() {
//        return new ProxyRepositoryBeanFactory();
//    }
//
//    @Bean
//    public ProxyRepositoryFactoryBean proxyRepositoryFactoryBean(ProxyRepositoryBeanFactory proxyRepositoryBeanFactory) {
//        return new ProxyRepositoryFactoryBean(proxyRepositoryBeanFactory);
//    }
//
//    // 동적으로 프록시 빈을 생성하고 등록하는 빈 팩토리
//    public static class ProxyRepositoryBeanFactory {
//        public void registerProxyRepositories() {
//            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
//
//            scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
//                // 현재 클래스가 리포지토리를 직접 상속받은 경우에만 true를 반환합니다.
//                try {
//                    Class<?>[] interfaces = Class.forName(metadataReader.getClassMetadata().getClassName()).getInterfaces();
//                    for (Class<?> iface : interfaces) {
//                        if (Repository.class.equals(iface)) {
//                            return true;
//                        }
//                    }
//                    return false;
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                    return false;
//                }
//            });
//
//            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("org.sam.store");
//            System.out.println(candidateComponents);
//            for (BeanDefinition bd : candidateComponents) {
//                try {
//                    Class<?> clazz = ClassUtils.forName(bd.getBeanClassName(), getClass().getClassLoader());
//                    ProxyRepository<?, ?> defaultMemoryRepository = new ProxyRepository<>();
//                    System.out.println(defaultMemoryRepository);
//                    Object proxyInstance = Proxy.newProxyInstance(
//                            Repository.class.getClassLoader(),
//                            new Class[]{clazz},
//                            defaultMemoryRepository
//                    );
//                    System.out.println(proxyInstance);
//                } catch (ClassNotFoundException e) {
//                    // 클래스를 찾을 수 없는 경우 처리
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    // 프록시 빈 등록을 위한 팩토리 빈
//    public static class ProxyRepositoryFactoryBean {
//        private final ProxyRepositoryBeanFactory proxyRepositoryBeanFactory;
//
//        public ProxyRepositoryFactoryBean(ProxyRepositoryBeanFactory proxyRepositoryBeanFactory) {
//            this.proxyRepositoryBeanFactory = proxyRepositoryBeanFactory;
//        }
//
//        @Bean
//        public void registerProxyRepositories() {
//            proxyRepositoryBeanFactory.registerProxyRepositories();
//        }
//    }

}
