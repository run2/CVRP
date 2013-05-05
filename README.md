CVRP
====

Solving capacitated vehicle routing problem with Genetic Algorithm.

This Java code base solves a Capacitated Vehicle Routing problem using Genetic Algorithm

For selectors it uses RouletteWheel and Stochastic Universal Sampling (http://ifigenia.org/w/images/2/2c/IWGN-2009-01-07.pdf)

For Cross Over it uses Sequential Constructive Crossover (http://www.cscjournals.org/csc/manuscript/Journals/IJBB/volume3/Issue6/IJBB-41.pdf)

SCC was found to be far more effective compared to Neighbourhood Search, SubRoute Insertion, Insertion and Saving heuristics,PMX cross over, K Means Clustering, Intra Route Exchange relocate etc.

It uses a custom - flip mutation(flips the whole solution), and the framework supports Shuffle SubTours and Shuffle Tour mutations.

It also uses Elitism and a Circle Sweep Optimization

The framework should work with any Genetic Algorithm with some minor changes.

The framework also solves Travelling SalesMan problems as a subset of CVRP problem and
shows how to solve the OneMax problem

Pseudo Code for CVRP
generate random new generation* of predefined population size
initialize last generation from random new generation
till termination condition reached
  store and cache last generation fitness and costs
  increase iteration count
  if reset condition reached
    generate new random generation* of half population size
    retrieve best of elite chromosomes** of half population size
    reinitialize last generation as a shuffle of the two above
  end
  check valid solutions and store elite chromosomes from last population****
  dispose current generation
  if reset condition was reached in this run
    choose mating population as last generation
  else
    choose mating population from last generation based on fitness
  end
  apply crossover to mating population to get current generation***
  apply mutation on current generation
  dispose last generation
  get current generation into last generation*****
optimize final elite chromosomes
*(chromosomes with random sequence of all nodes with depos inserted at equal sequential lengths)
**(conditionally optimize elite chromosomes before reintroduction)
***(conditionally generate 4 children from each two parents)
**** to a max of population size in a FIFO queue
***** choose best set if size is more than population size


More Information on this topic:
1)http://www.geatbx.com/docu/algindex-02.html#P416_20744
2)http://ifigenia.org/w/images/2/2c/IWGN-2009-01-07.pdf
3)http://www.cs.york.ac.uk/rts/docs/GECCO_2003/papers/2723/27230646.pdf
4)http://reference.kfupm.edu.sa/content/g/v/gvr__a_new_genetic_representation_for_th_56873.pdf
5)http://www.ima.umn.edu/talks/workshops/9-9-13.2002/savelsbergh/VRP_part1.pdf
6)http://www.youtube.com/watch?v=A1wsIFDKqBk
7)http://www.youtube.com/watch?v=LjvdXKsvUpU
8)http://www.youtube.com/watch?v=hjZDDz3r1es&feature=relmfu
9)http://arxiv.org/ftp/arxiv/papers/1001/1001.4197.pdf
10)http://www.rubicite.com/Tutorials/GeneticAlgorithms/CrossoverOperators/PMXCrossoverOperator.aspx
11)http://www.cscjournals.org/csc/manuscript/Journals/IJBB/volume3/Issue6/IJBB-41.pdf
12)http://bib.irb.hr/datoteka/433524.Vehnicle_Routing_Problem.pdf
13)http://www.ijest.info/docs/IJEST11-03-10-091.pdf
14)http://www.iba.t.u-tokyo.ac.jp/~jaku/pdf/rindoku101109_slide.pdf


