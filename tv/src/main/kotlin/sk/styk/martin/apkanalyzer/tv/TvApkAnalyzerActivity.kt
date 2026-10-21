package sk.styk.martin.apkanalyzer.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme

@AndroidEntryPoint
class TvApkAnalyzerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApkAnalyzerTvTheme {
                TvApkAnalyzerApp(onBackAtRoot = ::finish)
            }
        }
    }
}
