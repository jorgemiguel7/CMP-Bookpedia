import com.jorge.bookpedia.book.data.mappers.toBook
import com.jorge.bookpedia.book.data.source.remote.api.BookApi
import com.jorge.bookpedia.book.data.source.remote.datasource.RemoteBookDataSource
import com.jorge.bookpedia.book.data.source.remote.dto.BookWorkDto
import com.jorge.bookpedia.book.data.source.remote.dto.SearchResponseDto
import com.jorge.bookpedia.book.data.source.remote.dto.SearchedBookDto
import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.core.domain.DataError
import com.jorge.bookpedia.core.domain.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RemoteBookDataSourceTest {

    private val bookApi = mockk<BookApi>()
    private val remoteBookDataSource = RemoteBookDataSource(bookApi)

    @Test
    fun `given successful response When searchBooks is called Then returns list of books`() =
        runTest {
            val query = "kotlin"
            val resultLimit = 10
            val searchResponseDtoList = listOf(
                mockk<SearchedBookDto>(relaxed = true).copy(id = "1", title = "Kotlin in Action")
            )
            val expected = searchResponseDtoList.map { it.toBook() }
            coEvery {
                bookApi.searchBooks(query, resultLimit)
            } returns Result.Success(SearchResponseDto(results = searchResponseDtoList))

            val result = remoteBookDataSource.searchBooks(query, resultLimit)

            assertIs<Result.Success<List<Book>>>(result)
            val actual = result.data
            assertEquals(expected, actual)
        }

    @Test
    fun `given failure response When searchBooks is called Then returns error`() = runTest {
        val query = "kotlin"
        val resultLimit = 10
        val expected = DataError.Remote.UNKNOWN
        coEvery {
            bookApi.searchBooks(query, resultLimit)
        } returns Result.Error(expected)

        val result = remoteBookDataSource.searchBooks(query, resultLimit)

        assertIs<Result.Error<DataError>>(result)
        val actual = result.error
        assertEquals(expected, actual)
    }

    @Test
    fun `given successful response When getBookDetails is called Then returns book description`() =
        runTest {
            val bookWorkId = "1"
            val expected = "A book about Kotlin"
            coEvery {
                bookApi.getBookDetails(bookWorkId)
            } returns Result.Success(BookWorkDto(description = expected))

            val result = remoteBookDataSource.getBookDetails(bookWorkId)

            assertIs<Result.Success<String?>>(result)
            val actual = result.data
            assertEquals(expected, actual)
        }

    @Test
    fun `given failure response When getBookDetails is called Then returns error`() = runTest {
        val bookWorkId = "1"
        val expected = DataError.Remote.UNKNOWN
        coEvery {
            bookApi.getBookDetails(bookWorkId)
        } returns Result.Error(expected)

        val result = remoteBookDataSource.getBookDetails(bookWorkId)

        assertIs<Result.Error<DataError.Remote>>(result)
        val actual = result.error
        assertEquals(expected, actual)
    }
}