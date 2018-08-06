package com.main.popularmovies1;

import com.main.popularmovies1.components.home.model.Movie;
import com.main.popularmovies1.components.home.repository.Repository;
import com.main.popularmovies1.components.home.view.MainActivity;
import com.main.popularmovies1.components.home.view_model.MoviesViewModel;
import com.main.popularmovies1.databinding.ActivityMainBinding;
import com.main.popularmovies1.local_db.WishListDataBase;
import com.main.popularmovies1.network.ApiClient;
import com.main.popularmovies1.network.ApiInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Mock
    private Repository repository;
    private ApiInterface apiInterface;
    private MoviesViewModel moviesViewModel;
    @Mock
    private WishListDataBase wishListDataBase;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        //repository = Mockito.mock(Repository.class);
        //wishListDataBase = Mockito.mock(WishListDataBase.class);
        //apiInterface = Mockito.mock(ApiInterface.class);
        moviesViewModel = new MoviesViewModel();
    }

    @Test
    public void verifyRepositoryCall(){
        moviesViewModel.getMoviesFromServer(apiInterface, repository, 1);
        Mockito.verify(repository).getMovies(apiInterface,moviesViewModel,1);
    }

    @Test
    public void verifyRepositoryCallForFavouriteMovies(){
        moviesViewModel.getFavouriteMovies(wishListDataBase, repository);
        Mockito.verify(repository).getFavouriteMovies(wishListDataBase,moviesViewModel);
    }

}