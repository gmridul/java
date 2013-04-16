.SUFFIXES: .cpp .hpp

# Programs
SHELL 	= bash
CC     	= g++
LD	= ld
RM 	= rm
ECHO	= /bin/echo
CAT	= cat
PRINTF	= printf
SED	= sed
DOXYGEN = doxygen
######################################
# Project Name (generate executable with this name)

# Project Paths
PROJECT_ROOT = .
SRCDIR = $(PROJECT_ROOT)/src
DOCDIR = $(PROJECT_ROOT)/doc
CLASSDIR = $(PROJECT_ROOT)/class

######################################

NO_COLOR=\e[0m
OK_COLOR=\e[1;32m
ERR_COLOR=\e[1;31m
WARN_COLOR=\e[1;33m
MESG_COLOR=\e[1;34m
FILE_COLOR=\e[1;37m

OK_STRING="[OK]"
ERR_STRING="[ERRORS]"
WARN_STRING="[WARNINGS]"
OK_FMT="${OK_COLOR}%30s\n${NO_COLOR}"
ERR_FMT="${ERR_COLOR}%30s\n${NO_COLOR}"
WARN_FMT="${WARN_COLOR}%30s\n${NO_COLOR}"
######################################

SRCS := MorphGUI.java
OTHERSRCS := $(wildcard Jmorph/*.java)
CLASS := MorphGUI.class
.PHONY: all setup doc clean distclean

all: setup MorphGUI.class

setup:
	@$(ECHO) "Setting up compilation..."

doc : $(SRCDIR)/$(SRCS) $(SRCDIR)/$(OTHERSRCS)
	@cd $(SRCDIR); \
	javadoc -d ../doc *.java Jmorph/*.java

compile : $(SRCDIR)/$(SRCS) $(SRCDIR)/$(OTHERSRCS)
	@cd $(SRCDIR)/; \
	javac -d ../class MorphGUI.java 
	
clean:
	@$(ECHO) -n "Cleaning up..."
	@$(RM) -rf $(CLASSDIR)/*
	@$(RM) -rf images/test*
	@$(RM) -rf doc/*
	@$(ECHO) "Done"
