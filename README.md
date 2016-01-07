# Rapier
Dependency injection Framework for Android. No reflection, Generate java code in compile time, and Inject everywhere.

> __DO NOT use it in any production project until version 1.0 releases and publishes to maven central.__

- Special Module Class in `@Module` annotation, and inject objects use `@Inject` annotation.

```java
@Module(moduleClazz = MainModule.class)
public class MainActivity extends AppCompatActivity{
    @Inject
    IMainPresenter presenter;

    @Inject
    FooData fooData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity_Rapier.create().inject(new MainModule(this), this);

		fooData.getDataContent();
    }
```

- Module which will provider objects for injection:

```java
public class MainModule{
    private MainViewer mainViewer;

    public MainModule(MainViewer mainViewer) {
        this.mainViewer = mainViewer;
    }

    public IMainPresenter pickPresenter(){
        return new MainPresenter(mainViewer);
    }

    public FooData pickFooData(){
        return new FooData(112358, "hello foo");
    }
}
```

- Not only Activity injection, you can inject everywhere, such as Presenter:

```java
@Module(moduleClazz = MainPresenterModule.class)
public class MainPresenter implements IMainPresenter {
    private MainViewer viewer;

    @Inject
    PrefsHelper prefsHelper;

    @Inject
    List<FooData> testData;

    public MainPresenter(MainViewer viewer) {
        this.viewer = viewer;
        MainPresenter_Rapier.create().inject(new MainPresenterModule(), this);

        Log.i(TAG, "testData: " + testData);

    }
}
```
