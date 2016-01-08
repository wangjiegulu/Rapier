# Rapier
Dependency injection Framework for Android. No reflection, Generate java code in compile time, and Inject everywhere.

> __DO NOT use it in any production project until version 1.0 releases and publishes to maven central.__

- Special Module Class in `@RModule` annotation, and inject objects use `@RInject` annotation.

```java
@RModule(moduleClazz = MainModule.class)
public class MainActivity extends AppCompatActivity implements MainViewer, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @RInject
    IMainPresenter presenter;

    @RInject
    @RNamed(MainModule.FOO_DATA_A)
    FooData fooDataA;

    @RInject
    @RNamed(MainModule.FOO_DATA_B)
    FooData fooDataB;

    @RInject
//    @RNamed("error named")
    FooData fooDataNoNamed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity_Rapier.create().inject(new MainModule(this), this);

        Log.i(TAG, "fooDataA: " + fooDataA);
        Log.i(TAG, "fooDataB: " + fooDataB);
        Log.i(TAG, "fooDataNoNamed: " + fooDataNoNamed);

    }
}
```

- Module which will provider objects for injection:

```java
public class MainModule{
    public static final String FOO_DATA_A = "FOO_DATA_A";
    public static final String FOO_DATA_B = "FOO_DATA_B";

    private MainViewer mainViewer;

    public MainModule(MainViewer mainViewer) {
        this.mainViewer = mainViewer;
    }

    public IMainPresenter pickPresenter(){
        return new MainPresenter(mainViewer);
    }

    @RNamed(FOO_DATA_A)
    public FooData pickFooDataA(){
        return new FooData(112358, "hello foo A");
    }

    @RNamed(FOO_DATA_B)
    public FooData pickFooDataB(){
        return new FooData(11235813, "hello foo B");
    }

    public FooData pickFooDataNoNamed(){
        return new FooData(11235813, "hello foo no named");
    }
}
```

- Not only Activity injection, you can inject everywhere, such as Presenter:

```java
@RModule(moduleClazz = MainPresenterModule.class)
public class MainPresenter implements IMainPresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private MainViewer viewer;

    @RInject
    PrefsHelper prefsHelper;

    @RInject
    List<FooData> testData;

    public MainPresenter(MainViewer viewer) {
        this.viewer = viewer;
        MainPresenter_Rapier.create().inject(new MainPresenterModule(), this);
        Log.i(TAG, "testData: " + testData);
    }

    @Override
    public void saveFooData(FooData fooData) {
        prefsHelper.putInt(FooData.KEY_DATA_ID, fooData.getDataId())
                .putString(FooData.KEY_DATA_CONTENT, fooData.getDataContent())
                .commit();
        viewer.toast(fooData.getDataContent() + " saved!");
    }
}
```
