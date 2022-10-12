package com.example.pro_management

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.mobileapplicationdevelopmentcoursework.User.projectManager.projectManagerFragment

import com.example.pro_management.user.CurrentUser
import com.example.pro_management.user.UserHelper
import com.example.pro_management.login.LoginActivity
import com.example.pro_management.login.LoginNetwork
import com.example.pro_management.tasks.*
import com.example.pro_management.ui.MainMenuFragment
import com.example.pro_management.ui.SubmenuSortbyFragment
import com.example.pro_management.ui.UserFragment
import com.example.pro_management.util.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val mFragmentList: MutableList<Fragment?> = ArrayList()
    private lateinit var user: CurrentUser
    val mainmenu = MainMenuFragment()
    val submenuSort = SubmenuSortbyFragment()
    val userfrag = UserFragment()
    val sortKey:String = "Importance"
    private var mFragmentAdapter: FragmentAdapter? = null
    var proLs = mutableListOf<Project>()
    private var accountTextManager: TextView? = null
    private var projectListTextManager: TextView? = null
    private var vp: ViewPager? = null
    private var projectListManager: projectManagerFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
       EventBus.getDefault().register(this )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //test data
        //val mock = MockHelper()

        user = CurrentUser.getUser()

        AppConfig.init(this)

        if(AppConfig.config.getValue("runmode") == "demo"){

        }       //set run mode  standard/demo
        else{
            // check the user SharedPreferences
            val sps = this.getSharedPreferences("user", Context.MODE_PRIVATE)
            val account = sps.getString("email", "")
            val passwd = sps.getString("passwd", "")
            val username = sps.getString("username", "")

            val currUser = CurrentUser.getUser()
            currUser.Unlock()

            val login = LoginNetwork()

            if(account.isNullOrBlank()){
                Toast.makeText(this, "Please, Login", Toast.LENGTH_SHORT).show()

                val loginIntent = Intent()
                loginIntent.setClass(this, LoginActivity::class.java)
                startActivityForResult(loginIntent, 200)
            }       // no user signed in before, go to sign in page
            else{
                currUser.ResetUser(4, username!!, account, passwd!!, "")
                login.login(account, passwd!!,  1)


                if(UserHelper.loadLocalUser(user, this)){
                    Toast.makeText(this, "Welcome, " + user.getUsername(), Toast.LENGTH_SHORT).show()
                }else{


                    val loginIntent = Intent()
                    loginIntent.setClass(this, LoginActivity::class.java)
                    startActivityForResult(loginIntent, 200)
                }
            }                              // user have signed in before, login automatically
        }

        init()
        initViews()

        vp!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                changeTextColor(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })


    }

    override fun onClick(v: View) {
        when (v.id) {
            //R.id.Account -> vp!!.setCurrentItem(0, true)
            //R.id.ProjectList -> vp!!.setCurrentItem(1, true)
        }
    }


    fun loaddata(){
        mFragmentAdapter = FragmentAdapter(this.supportFragmentManager, mFragmentList)
        vp!!.offscreenPageLimit = 2
        vp!!.adapter = mFragmentAdapter
        vp!!.currentItem = 1
        projectListTextManager!!.setTextColor(Color.parseColor("#1ba0e1"))
    }
    

    private fun initViews() {
        projectListTextManager = findViewById<View>(R.id.ProjectList) as TextView
        projectListTextManager!!.setOnClickListener(this)
        vp = findViewById<View>(R.id.mainViewPager) as ViewPager
        projectListManager = projectManagerFragment()
        mFragmentList.add(projectListManager)
    }

    private fun changeTextColor(position: Int) {
        if (position == 0) {
            accountTextManager!!.setTextColor(Color.parseColor("#1ba0e1"))
            projectListTextManager!!.setTextColor(Color.parseColor("#000000"))
        } else if (position == 1) {
            accountTextManager!!.setTextColor(Color.parseColor("#000000"))
            projectListTextManager!!.setTextColor(Color.parseColor("#1ba0e1"))
        }
    }

    //Register btn listeners and widgets in main page
    private fun init(){
        val dayOfWeek = MyDateTime.GetDayOfWeek()
        val str_date = MyDateTime.GetDayOfMonth()

        findViewById<TextView>(R.id.txt_date).setText(dayOfWeek+", "+str_date)

        val s = CurrentUser.getUser()

        //register listeners
        findViewById<ImageButton>(R.id.btn_main_back).setOnClickListener(
            View.OnClickListener {
                onMainBack()
            }
        )

        findViewById<ImageButton>(R.id.btn_idea).setOnClickListener{
            onIdea()
        }

        findViewById<Button>(R.id.btn_create_project).setOnClickListener{
            onNewProject()
        }

         findViewById<ImageButton>(R.id.btn_main_menu).setOnClickListener(
            View.OnClickListener {
                onMainMenu()
            }
        )

        findViewById<ImageButton>(R.id.btn_role).setOnClickListener(
            View.OnClickListener { view->
                onUserRole()
            }
        )
    }

    fun onIdea(){

    }

    private fun onMainMenu() {
        supportFragmentManager.beginTransaction().add(mainmenu, MainMenuFragment.TAG).commit()
    }

    private fun onMainBack(){
        val te = TextView(this)
        val layout = findViewById<ConstraintLayout>(R.id.master_layout)
    }

    private fun onUserRole(){
        supportFragmentManager.beginTransaction().add(userfrag, UserFragment.TAG).commit()
    }

    private fun onNewProject(){
        val intent = Intent()
        intent.setClass(this, NewProject::class.java)
        startActivityForResult(intent, 204)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageMainmenu(event: MainmenuItem){
        if(event.msg == "Sort by"){
            supportFragmentManager.beginTransaction().remove(mainmenu).commit()
            supportFragmentManager.beginTransaction().add(submenuSort, SubmenuSortbyFragment.TAG).commit()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageSubmenuSort(event: MessageSortBy){
        if(event.msg == "Back to main"){
            supportFragmentManager.beginTransaction().remove(submenuSort).commit()
            supportFragmentManager.beginTransaction().add(mainmenu, MainMenuFragment.TAG).commit()
        }else if(event.msg == "Due Date"){
            supportFragmentManager.beginTransaction().remove(submenuSort).commit()

            var tempLs = mutableListOf<Project1>()
            MyDateTime.copyProtoLDT(proLs, tempLs)
            var temp2 = (tempLs.sortedBy({it.project_deadline})).toMutableList()
            proLs.clear()
            MyDateTime.copyProToStr(proLs, temp2)
            loaddata()

        }else if(event.msg == "Alphabet"){
            supportFragmentManager.beginTransaction().remove(submenuSort).commit()
            var tempLs = proLs.sortedBy({it.project_name})
            proLs.clear()
            proLs = tempLs.toMutableList()
            loaddata()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 200 && resultCode == 201){
            // Login Activity
            var ac_res = data?.getIntExtra("login_res", -1)
            val correntUser = CurrentUser.getUser()

            Toast.makeText(this,"Welcome, "+correntUser.getUsername() , Toast.LENGTH_SHORT).show()
        }

        if(requestCode == 204 && resultCode ==205){
//            Toast.makeText(this, "Created!", Toast.LENGTH_SHORT).show()


            val num = data?.getIntExtra("task_num", 0)
            Toast.makeText(this, num.toString(), Toast.LENGTH_SHORT).show()

            queryPro()
        }

        if(resultCode == 501){

            queryPro()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onQueryPro(event:AutoLoginRes){
        println(event.msg)
        queryPro()

        if(CurrentUser.getUser().isManager()){
            findViewById<ImageButton>(R.id.btn_role).setBackgroundResource(R.drawable.manager)

        }else{
            findViewById<Button>(R.id.btn_create_project).isVisible = false
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTaskQuery(event: ProQueryResult){
        loaddata()
    }

    fun queryPro(){
        proLs.clear()
        TaskHelper.queryPro(proLs)
        loaddata()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSignout(event: Signout){
        if(event.code == 0){
            supportFragmentManager.beginTransaction().remove(userfrag).commit()

            CurrentUser.getUser().signout(this)

            val loginIntent = Intent()
            loginIntent.setClass(this, LoginActivity::class.java)
            startActivityForResult(loginIntent, 200)
        }else if(event.code == -1){
            Toast.makeText(this, "signout failed", Toast.LENGTH_SHORT).show()
            var sps=this.getSharedPreferences("user", Context.MODE_PRIVATE)
            val editor = sps.edit()
            editor.clear()
            editor.apply()

            val loginIntent = Intent()
            loginIntent.setClass(this, LoginActivity::class.java)
            startActivityForResult(loginIntent, 200)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    inner class FragmentAdapter(fm: FragmentManager?, fragmentList: List<Fragment?>) :
        FragmentPagerAdapter(fm!!) {
        var fragmentList: List<Fragment> = ArrayList()
        override fun getItem(position: Int): Fragment {

            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        init {
            this.fragmentList = fragmentList as List<Fragment>
        }
    }
}