/**
 * 动态代理：
 *  特点：字节码随用随创建，随用随加载
 *  作用：不修改源码的基础上对方法增强
 *  分类：
 *      基于接口的动态代理
 *      基于子类的动态代理
 *  基于子类的动态代理：
 *      涉及的类：Enhancer
 *      提供者：第三方cglib库
 *  如何创建代理对象：
 *      使用Enhancer类中的create方法
 *  创建代理对象的要求：
 *      被代理类不能是最终类
 *  newProxyInstance方法的参数：在使用代理时需要转换成指定的对象
 *      ClassLoader:类加载器
 *          他是用于加载代理对象字节码的。和被代理对象使用相同的类加载器。固定写法
 *      Callback：用于提供增强的代码
 *          他是让我们写如何代理。我们一般写一个该接口的实现类，通常情况加都是匿名内部类，但不是必须的。
 *          此接口的实现类，是谁用谁写。
 *          我们一般写的都是该接口的子接口实现类，MethodInterceptor
 */
com.dynamic.cglib.Producer cglibProducer= (com.dynamic.cglib.Producer) Enhancer.create(
        com.dynamic.cglib.Producer.class,
        new MethodInterceptor() {
            /**
             *  执行被代理对象的任何方法都会经过该方法
             * @param obj
             * @param method
             * @param args
             *      以上三个参数和基于接口的动态代理中invoke方法的参数是一样的
             * @param proxy：当前执行方法的代理对象
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                Object returnValue=null;
                Float money=(Float)args[0];
                if("saleProduct".equals(method.getName())){
                   returnValue= method.invoke(producer,money*0.8f);
                }
                return returnValue;
            }
        }
);
cglibProducer.saleProduct(100.0f);
