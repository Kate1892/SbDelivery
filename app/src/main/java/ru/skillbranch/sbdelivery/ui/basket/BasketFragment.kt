package ru.skillbranch.sbdelivery.ui.basket

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.android.ext.android.inject
import ru.skillbranch.sbdelivery.core.notifier.BasketNotifier
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent
import ru.skillbranch.sbdelivery.databinding.FragmentBasketBinding

class BasketFragment : Fragment() {
    companion object {
        fun newInstance() = BasketFragment()
    }

    private val notifier: BasketNotifier by inject()
    private lateinit var disposable: Disposable
    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable = notifier.eventSubscribe()
            .subscribe {
                if (it is BasketEvent.AddDish) {
                    binding.tvDishes.text =
                        "${binding.tvDishes.text}\n\n ${it.title} стоимость ${it.price}"
                }

            }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) disposable.dispose()
    }
}