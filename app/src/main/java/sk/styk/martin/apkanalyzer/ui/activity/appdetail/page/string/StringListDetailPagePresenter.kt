package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class StringListDetailPagePresenter : StringListDetailPageContract.Presenter {

    override lateinit var view: StringListDetailPageContract.View
    private lateinit var values: List<String>

    override fun initialize(data: List<String>) {
        this.values = data
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = values.size

    override fun onBindViewOnPosition(position: Int, holder: StringListDetailPageContract.ItemView) {
        holder.bind(values[position])
    }

}