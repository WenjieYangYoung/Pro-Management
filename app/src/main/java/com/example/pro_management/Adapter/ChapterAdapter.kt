//package com.example.mobileapplicationdevelopmentcoursework.Adapter
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.graphics.Color
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import com.example.pro_management.R
//import com.example.pro_management.databinding.ProjectListBinding
//import com.example.pro_management.databinding.TaskListBinding
//
//class ChapterAdapter(private val mContext: Context, private val mData: List<Chapter>) :
//    BaseExpandableListAdapter() {
//
//    private var childViewHolder: ChildViewHolder? = null
//    private var groupViewHolder: ParentViewHolder? = null
//    private var _binding: TaskListBinding? = null
//    private var binding: ProjectListBinding? = null
//    var projectPosition: Int? = null
//    var taskPosition: Int? = null
//
//
//    override fun getGroupCount(): Int {// number of projects
//        return mData.size
//    }
//
//    override fun getChildrenCount(groupPosition: Int): Int { // number of tasks under each project
//        return mData[groupPosition].getChildren().size
//    }
//
//    override fun getGroup(groupPosition: Int): Any { //get one project
//        return mData[groupPosition]
//    }
//
//    override fun getChild(groupPosition: Int, childPosition: Int): Any { // get one task under one project
//        return mData[groupPosition].getChildren()[childPosition]
//    }
//
//    override fun getGroupId(groupPosition: Int): Long { // get the row value of project
//        return groupPosition.toLong()
//    }
//
//    override fun getChildId(groupPosition: Int, childPosition: Int): Long { // get the row value of task
//        return childPosition.toLong()
//    }
//
//    // TODO
//    override fun hasStableIds(): Boolean {
//        return true
//    }
//
//    override fun getGroupView(
//        groupPosition: Int,
//        isExpanded: Boolean,
//        convertView: View?,
//        parent: ViewGroup?
//    ): View {
//        var mconvertView = convertView
////        var groupViewHolder: ParentViewHolder? = null
//
//        if (mconvertView == null) {//如果为空，则创建
//            // 修改item height即可演示，第二个参数作用
//            mconvertView = LayoutInflater.from(parent?.context)
//                .inflate(R.layout.project_list, parent, false)
//            groupViewHolder = ParentViewHolder()
//            groupViewHolder!!.projectName = mconvertView.findViewById(R.id.projectName)
//            groupViewHolder!!.projectDeadline = mconvertView.findViewById(R.id.projectDeadline)
//            groupViewHolder!!.projectManager = mconvertView.findViewById(R.id.projectManager)
//            groupViewHolder!!.teamMembers = mconvertView.findViewById(R.id.projectDescription)
////            groupViewHolder!!.parentImageView = mconvertView.findViewById(R.id.id_indicator_group)
//            mconvertView?.tag = groupViewHolder
//        } else {//如果存在则复用
//            groupViewHolder = mconvertView.tag as ParentViewHolder
//        }
//        groupViewHolder!!.projectName!!.text = mData[groupPosition].projectName
//        groupViewHolder!!.projectDeadline!!.text = mData[groupPosition].projectDeadline
//        groupViewHolder!!.projectManager!!.text = mData[groupPosition].projectManager
//        groupViewHolder!!.teamMembers!!.text = mData[groupPosition].teamMembers
//        groupViewHolder!!.parentImageView!!.isSelected = isExpanded
//
//        if (mData[groupPosition].projectStatus == "Ongoing"){
//            groupViewHolder!!.projectName?.setTextColor(Color.parseColor("#FF000000"))
//            groupViewHolder!!.projectDeadline?.setTextColor(Color.parseColor("#CC0000"))
//            notifyDataSetChanged()
//        }
//        else if (mData[groupPosition].projectStatus == "Complete"){
//            groupViewHolder!!.projectName?.setTextColor(Color.parseColor("#3FE2C5"))
//            groupViewHolder!!.projectDeadline?.setTextColor(Color.parseColor("#FF000000"))
//            notifyDataSetChanged()
//        }
//        return mconvertView!!
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun getChildView(
//        groupPosition: Int,
//        childPosition: Int,
//        isLastChild: Boolean,
//        convertView: View?,
//        child: ViewGroup?
//    ): View {
//        var mconvertView = convertView
////        var childViewHolder: ChildViewHolder? = null
//
//        if (mconvertView == null) {//如果为空，则创建
//            mconvertView = LayoutInflater.from(child?.context)
//                .inflate(R.layout.task_list, child, false)
//            childViewHolder = ChildViewHolder()
//            childViewHolder!!.taskName = mconvertView.findViewById(R.id.taskName)
//            childViewHolder!!.taskDeadline = mconvertView.findViewById(R.id.taskDeadline)
//            childViewHolder!!.taskMember = mconvertView.findViewById(R.id.taskMember)
//            childViewHolder!!.taskStatus = mconvertView.findViewById(R.id.taskStatus)
//            mconvertView?.tag = childViewHolder
//
//            _binding = TaskListBinding.inflate(LayoutInflater.from(child?.context), child, false)
//            val root: View = _binding!!.root
//
//        } else {//如果存在则复用
//            childViewHolder = mconvertView.tag as ChildViewHolder
//        }
//        childViewHolder!!.taskName!!.text = mData[groupPosition].getChildren()[childPosition].taskName
//        childViewHolder!!.taskDeadline!!.text = mData[groupPosition].getChildren()[childPosition].taskDeadline
//        childViewHolder!!.taskMember!!.text = mData[groupPosition].getChildren()[childPosition].taskMember
////        if(mData[groupPosition].getChildren()[childPosition].taskStatus == "Ongoing"){
////            childViewHolder!!.taskStatus!!.text = "Ongoing"
////        }else
////        if(mData[groupPosition].getChildren()[childPosition].taskStatus == "Complete"){
////            childViewHolder!!.taskStatus!!.text = "Complete"
////        }
////        childViewHolder!!.taskStatus!!.text = mData[groupPosition].getChildren()[childPosition].taskStatus
//
////        var count: Int? = null
//        childViewHolder!!.taskStatus?.setOnCheckedChangeListener(null);
//        childViewHolder!!.taskStatus!!.setOnCheckedChangeListener { _, isChecked ->
//            childViewHolder!!.count = 0
//            val arrayTasks: Array<Int?> = emptyArray()
//            if (isChecked) {
//                mData[groupPosition].getChildren()[childPosition].taskStatus = "Complete"
//                notifyDataSetChanged()
////                childViewHolder!!.taskStatus!!.setTextColor(Color.parseColor("#FF000000"))
////                Log.d("zhy", "onChildClick groupPosition = "
////                        + groupPosition + " , childPosition = " + childPosition + " , value = " + mData[groupPosition].getChildren()[childPosition].taskStatus)
//            }
//            else {
//                mData[groupPosition].getChildren()[childPosition].taskStatus = "Ongoing"
//                notifyDataSetChanged()
////                childViewHolder!!.taskStatus!!.setTextColor(Color.parseColor("#CC0000"))
////                Log.d("zhy", "onChildClick groupPosition = "
////                        + groupPosition + " , childPosition = " + childPosition + " , value = " + mData[groupPosition].getChildren()[childPosition].taskStatus)
//            }
//
//            // 判断所有的task是否都为complete
//            for (i in mData[groupPosition].getChildren()){
//                if (i.taskStatus == "Complete"){
//                    Log.d("zhy", "task: " + i +"taskStatus = " + i.taskStatus!!)
//                    childViewHolder!!.count = childViewHolder!!.count!! + 1
//                }
//                else {
//                    Log.d("zhy", "task: " + i +"taskStatus = " + i.taskStatus!!)
//                }
//            }
//            if (childViewHolder!!.count!! < mData[groupPosition].getChildren().size){
//                mData[groupPosition].projectStatus = "Ongoing"
//                notifyDataSetChanged() // Important, automatically update
//            } else{
//                mData[groupPosition].projectStatus = "Complete"
//                notifyDataSetChanged() // Important, automatically update
//            }
//            Log.d("zhy", "count = " + childViewHolder!!.count)
//        }
//        return mconvertView!!
//    }
//
//    // 控制child item不可点击
//    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
//        return true
//    }
//
//    class ParentViewHolder {
//        var projectName: TextView? = null
//        var projectDeadline: TextView? = null
//        var projectManager: TextView? = null
//        var teamMembers: TextView? = null
//        var parentImageView: ImageView? = null
//    }
//
//    class ChildViewHolder {
//        var taskName: TextView? = null
//        var taskDeadline: TextView? = null
//        var taskMember: TextView? = null
//        var taskStatus: CheckBox? = null
//        var count: Int? = null
//    }
//
//}
