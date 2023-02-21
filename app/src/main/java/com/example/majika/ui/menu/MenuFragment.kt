package com.example.majika.ui.menu

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.MainActivity
import com.example.majika.MajikaApplication
import com.example.majika.R
import com.example.majika.databinding.FragmentMenuBinding
import com.example.majika.model.Menu
import com.example.majika.ui.cart.CartViewModel
import com.example.majika.ui.cart.CartViewModelFactory

class MenuFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentMenuBinding? = null
    val cartViewModel: CartViewModel by viewModels { CartViewModelFactory((this.requireActivity().application as MajikaApplication).repository) }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var fragmentFoodData: List<Menu> = emptyList()
    private var fragmentDrinksData: List<Menu> = emptyList()

    private lateinit var sensorManager : SensorManager
    private var temp: Sensor? = null

    private lateinit var foodAdapter: MenuAdapter
    private lateinit var drinksAdapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        set view in the activity
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        recycle view for menu
        val menuViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)

        val menuSearch: SearchView = binding.menuSearch

        val foodRv: RecyclerView = binding.foodRv
        val foodError: LinearLayout = binding.foodError

        foodAdapter = MenuAdapter(activity as Context, cartViewModel)
        foodRv.layoutManager = LinearLayoutManager(this.requireContext())
        foodRv.adapter = foodAdapter

        menuViewModel.foodList.observe(viewLifecycleOwner) {
            foodAdapter.setMenuData(it.menuList!!.filter{ item -> item.name!!.contains(menuSearch.query!!, true) })

            if (foodAdapter.itemCount == 0) {
                foodError.visibility = View.VISIBLE
                foodRv.visibility = View.GONE
            } else {
                foodError.visibility = View.GONE
                foodRv.visibility = View.VISIBLE
            }
            fragmentFoodData = it.menuList
        }

        menuViewModel.getFood()

        val drinksRv: RecyclerView = binding.drinksRv
        val drinksError: LinearLayout = binding.drinksError

        drinksAdapter = MenuAdapter(activity as Context, cartViewModel)
        drinksRv.layoutManager = LinearLayoutManager(this.requireContext())
        drinksRv.adapter = drinksAdapter
        menuViewModel.drinksList.observe(viewLifecycleOwner) {
            drinksAdapter.setMenuData(it.menuList!!.filter{ item -> item.name!!.contains(menuSearch.query!!, true) })

            if (drinksAdapter.itemCount == 0) {
                drinksError.visibility = View.VISIBLE
                drinksRv.visibility = View.GONE
            } else {
                drinksError.visibility = View.GONE
                drinksRv.visibility = View.VISIBLE
            }

            fragmentDrinksData = it.menuList
        }

        menuViewModel.getDrinks()

        menuSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredFoodList = fragmentFoodData.filter {
                    it.name!!.contains(newText!!, true)
                }
                foodAdapter.setMenuData(filteredFoodList)


                if (filteredFoodList.isEmpty()) {
                    foodError.visibility = View.VISIBLE
                    foodRv.visibility = View.GONE
                } else {
                    foodError.visibility = View.GONE
                    foodRv.visibility = View.VISIBLE
                }

                val filteredDrinksList = fragmentDrinksData.filter {
                    it.name!!.contains(newText!!, true)
                }
                drinksAdapter.setMenuData(filteredDrinksList)

                if (filteredDrinksList.isEmpty()) {
                    drinksError.visibility = View.VISIBLE
                    drinksRv.visibility = View.GONE
                } else {
                    drinksError.visibility = View.GONE
                    drinksRv.visibility = View.VISIBLE
                }

                return false
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        sensorManager = this.requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (temp != null){
            sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this.requireActivity(), "No temperature sensor detected!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setTitle(getString(R.string.title_menu))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onSensorChanged(event: SensorEvent) {
        val temp = event.values[0]
        val view = (activity as AppCompatActivity).supportActionBar?.customView

        val temperatureText: TextView = view!!.findViewById(R.id.temperature_text)
        temperatureText.text = getString(R.string.temperature, temp)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}