
<img src="images/preview.gif" >

# Usage

*For a working implementation, please have a look at the Sample Project - sample*

1. Add it in your root build.gradle at the end of repositories:

	```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	```
2. Add the dependency
```
dependencies {
		implementation 'com.github.VIKAS9899:Gally:1.0'
	}
```
3. Add MediaStoreActivity into your AndroidManifest.xml

    ```
    <activity android:name="com.vikas.gally.activity.MediaStoreActivity"
            android:screenOrientation="portrait"/>
    ```
5. sdcard Read,Write Permission into AndroidManifest.xml
    ```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    ```

4. The Gally configuration is created using the builder pattern.

    ```java
    Decorator decorator = new Decorator.Builder()
                .setMaxSelection(7)
                .setTitle("GallySample")
                .setTickColor(R.color.colorAccent)
                .build();
    ```

5. Override `onActivityResult` method and handle result.

    ```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MediaStoreActivity.REQUEST_CODE_LAUNCH && resultCode == RESULT_OK) {

            ArrayList<String> selectedPath = data.getStringArrayListExtra(MediaStoreActivity.EXTRA_IMAGE_PATHS);
            System.out.println(selectedPath);
            //do Anything with the selected images
        }
    }
    ```
  
# Changelog

### Version: 1.0

  * Initial Build

### Let us know!

We’d be really happy if you sent us links to your projects where you use our component. Just send an email to sharma_vikas@yahoo.com And do let us know if you have any questions or suggestion regarding the Utility. 

## License

    Copyright 2017, Yalantis

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
