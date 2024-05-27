import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.training.movieappdbcppermission.databinding.DialogAddMovieBinding

class AddMovieDialogFragment(private val onMovieAdded: (String, String, String, Float) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddMovieBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Movie")
            .setView(binding.root)
            .setPositiveButton("Add") { dialog, _ ->
                val title = binding.etTitle.text.toString()
                val year = binding.etYear.text.toString()
                val genre = binding.etGenre.text.toString()
                val rating = binding.etRating.text.toString().toFloatOrNull()

                if (title.isNotEmpty() && year.isNotEmpty() && genre.isNotEmpty() && rating != null) {
                    onMovieAdded(title, year, genre, rating)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}
