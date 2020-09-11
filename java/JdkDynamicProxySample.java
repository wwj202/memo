/**
         * 动态代理：
         *  特点：字节码随用随创建，随用随加载
         *  作用：不修改源码的基础上对方法增强
         *  分类：
         *      基于接口的动态代理
         *  基于接口的动态代理：
         *      涉及的类：proxy
         *      提供者：Jdk官方
         *  如何创建代理对象：
         *      使用Proxy类中的newProxyInstance方法
         *  创建代理对象的要求：
         *      被代理类最少实现一个接口，如果没有则不能使用
         *  newProxyInstance方法的参数：在使用代理时需要转换成指定的对象
         *      ClassLoader:类加载器
         *          他是用于加载代理对象字节码的。和被代理对象使用相同的类加载器。固定写法
         *      Class[]：字节码数组
         *          它是用于让代理对象和被代理对象有相同方法。固定写法
         *      InvocationHandler：用于提供增强的代码
         *          他是让我们写如何代理。我们一般写一个该接口的实现类，通常情况加都是匿名内部类，但不是必须的。
         *          此接口的实现类，是谁用谁写。
         */
       IProducer proxyProducer=  (IProducer) Proxy.newProxyInstance(
                producer.getClass().getClassLoader(),
                producer.getClass().getInterfaces(),

               new InvocationHandler() {
                   /**
                    * 作用：执行被代理对象的任何接口方法都会经过该方法
                    * @param proxy  代理对象的引用
                    * @param method 当前执行的方法
                    * @param args   当前执行方法所需的参数
                    * @return       和被代理对象有相同返回值
                    * @throws Throwable
                    */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        提供增强的代码
//                        1、获取方法执行的参数
                        Object returnValue=null;
                        Float money=(Float)args[0];
                        if("saleProduct".equals(method.getName())){
                           returnValue= method.invoke(producer,money*0.8f);
                        }
                        return returnValue;
                    }
                }
        );
