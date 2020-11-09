package rat.poison.ui.tabs.rankstabs

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.kotcrab.vis.ui.widget.LinkLabel
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import rat.poison.curSettings
import rat.poison.scripts.playerList
import rat.poison.toLocale
import rat.poison.ui.uiHelpers.ranksTab.RanksVisCheckBoxCustom
import rat.poison.ui.uiRefreshing
import rat.poison.utils.RanksPlayer
import rat.poison.utils.generalUtil.strToBool

var enableEspPlayerList = mutableListOf<Int>()

class MainRanksTab : Tab(false, false) {
    private val table = VisTable(true)

    var ranksListTable = VisTable()
    var teamsLabel = VisLabel("Team".toLocale() + "  \n")

    var namesTable = VisTable()
    var namesLabel = VisLabel("Name")
    // if R.A.T.T.O wont do proper selection idc then

    var ranksLabel = VisLabel("Rank".toLocale() + "  \n")
    var killsLabel = VisLabel("Kills".toLocale() + "  \n")
    var deathsLabel = VisLabel("Deaths".toLocale() + "  \n")
    var kdLabel = VisLabel("K/D".toLocale() + "  \n")
    var winsLabel = VisLabel("Wins".toLocale() + "  \n")
    var moneyLabel = VisLabel("Money".toLocale() + "  \n")
    val espLabel = VisLabel("ESP")
    val espCheckBoxes = VisTable()

    init {
        table.padLeft(25F)
        table.padRight(25F)

        if (curSettings["RANKS_TAB_DISPLAY_TEAM"].strToBool())   { ranksListTable.add(teamsLabel) }

        if (curSettings["RANKS_TAB_DISPLAY_NAME"].strToBool())   { namesTable.add(namesLabel).row() }
        if (curSettings["RANKS_TAB_DISPLAY_NAME"].strToBool())   { ranksListTable.add(namesTable).top().padRight(4f) } //Table

        if (curSettings["RANKS_TAB_DISPLAY_RANK"].strToBool())   { ranksListTable.add(ranksLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_KILLS"].strToBool())  { ranksListTable.add(killsLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_DEATHS"].strToBool()) { ranksListTable.add(deathsLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_KD"].strToBool())     { ranksListTable.add(kdLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_WINS"].strToBool())   { ranksListTable.add(winsLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_MONEY"].strToBool())  { ranksListTable.add(moneyLabel) }
        espCheckBoxes.add(espLabel).top().row()
        ranksListTable.add(espCheckBoxes).top()


        table.add(ranksListTable).left().maxWidth(500F)
    }

    override fun getContentTable(): Table? {
        return table
    }

    override fun getTabTitle(): String? {
        return "Ranks".toLocale()
    }

    fun rebuildTable() {
        if (uiRefreshing) return
        uiRefreshing = true
        ranksListTable.clear()
        if (curSettings["RANKS_TAB_DISPLAY_TEAM"].strToBool())   { ranksListTable.add(teamsLabel) }

        if (curSettings["RANKS_TAB_DISPLAY_NAME"].strToBool())   { namesTable.add(namesLabel).row() }
        if (curSettings["RANKS_TAB_DISPLAY_NAME"].strToBool())   { ranksListTable.add(namesTable).top().padRight(4f) } //Table

        if (curSettings["RANKS_TAB_DISPLAY_RANK"].strToBool())   { ranksListTable.add(ranksLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_KILLS"].strToBool())  { ranksListTable.add(killsLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_DEATHS"].strToBool()) { ranksListTable.add(deathsLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_KD"].strToBool())     { ranksListTable.add(kdLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_WINS"].strToBool())   { ranksListTable.add(winsLabel) }
        if (curSettings["RANKS_TAB_DISPLAY_MONEY"].strToBool())  { ranksListTable.add(moneyLabel) }
        espCheckBoxes.add(espLabel).top().row()
        ranksListTable.add(espCheckBoxes).top()

        uiRefreshing = false
    }

    fun updateRanks() {
        if (uiRefreshing) return
        if (curSettings["RANKS_TAB_DISPLAY_TEAM"].strToBool())   { teamsLabel.setText("Team".toLocale() + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_NAME"].strToBool())   { namesLabel.setText("Name".toLocale()) }
        if (curSettings["RANKS_TAB_DISPLAY_RANK"].strToBool())   { ranksLabel.setText("Rank".toLocale() + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_KILLS"].strToBool())  { killsLabel.setText("Kills".toLocale() + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_DEATHS"].strToBool()) { deathsLabel.setText("Deaths".toLocale() + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_KD"].strToBool())     { kdLabel.setText("K/D".toLocale() + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_WINS"].strToBool())   { winsLabel.setText("Wins".toLocale() + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_MONEY"].strToBool())  { moneyLabel.setText("Money".toLocale() + "  \n") }

        namesTable.reset()
        espCheckBoxes.reset()
        espCheckBoxes.add(espLabel).top().row()
        namesTable.add(namesLabel).left().row()
        playerList.forEach { player ->
            when (player.team) {
                3L -> constructRank(player)
                2L -> constructRank(player)
            }
        }
    }
    private fun constructRank(player: RanksPlayer) {
        if (uiRefreshing) return
        var tmpName = player.name
        tmpName = sliceName(tmpName)
        val steamID = player.steamID.toInt()
        if (curSettings["RANKS_TAB_DISPLAY_NAME"].strToBool()) {
            if (steamID != 0) { //Bot check
                //map is not needed since we are calling clear()
                val linkLabel = LinkLabel(tmpName, steamID)
                linkLabel.setText(tmpName)
                namesTable.add(linkLabel).height(21f).left().row()
            } else {
                namesTable.add(tmpName).height(21f).left().row()
            }
        }
        if (curSettings["RANKS_TAB_DISPLAY_TEAM"].strToBool())   { teamsLabel.setText(teamsLabel.text.toString() + player.teamStr.toLocale() + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_RANK"].strToBool())   { ranksLabel.setText(ranksLabel.text.toString() + player.rank + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_KILLS"].strToBool())  { killsLabel.setText(killsLabel.text.toString() + player.kills + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_DEATHS"].strToBool()) { deathsLabel.setText(deathsLabel.text.toString() + player.deaths + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_KD"].strToBool())     { kdLabel.setText(kdLabel.text.toString() + player.KD + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_WINS"].strToBool())   { winsLabel.setText(winsLabel.text.toString() + player.wins + "  \n") }
        if (curSettings["RANKS_TAB_DISPLAY_MONEY"].strToBool())  { moneyLabel.setText(moneyLabel.text.toString() + player.money + "  \n") }
        espCheckBoxes.add(RanksVisCheckBoxCustom(player.steamID.toInt())).height(21F).left().row()
    }
}

fun sliceName(tmpName: String): String {
    if (curSettings["RANKS_TAB_ENABLE_LIMIT"].strToBool() && tmpName.length >= curSettings["RANKS_TAB_CHAR_LIMIT"].toInt()) {
        return tmpName.substring(0, curSettings["RANKS_TAB_CHAR_LIMIT"].toInt())
    }
    return tmpName
}