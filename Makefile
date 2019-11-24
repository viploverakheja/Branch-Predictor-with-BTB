JAVAC = javac
DEBUG = -g
CFLAGS = $(DEBUG) -deprecation

sim_cache:
	$(JAVAC) $(CFLAGS) sim.java

clean:
	-rm *.class
